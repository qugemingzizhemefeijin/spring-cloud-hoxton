package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.service.ElasticService;
import com.atguigu.springcloud.utils.BeanUtilsTool;
import com.atguigu.springcloud.vo.StudentVO;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.pipeline.bucketselector.BucketSelectorPipelineAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service
@Slf4j
public class ElasticServiceImpl implements ElasticService {

    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void helloWorld() {
        System.out.println("======");
    }

    @Override
    public void createIndex(String indexName, List<StudentVO> studentList) {
        BulkRequest request = new BulkRequest();

        for (StudentVO student : studentList) {
            request.add(new IndexRequest(indexName, "index").source(BeanUtilsTool.objectToMap(student)));
        }

        try {
            BulkResponse response = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
            if (response.hasFailures()) {
                exceptionRetry(request, response);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 异常捕获用于重试
     */
    private void exceptionRetry(BulkRequest request, BulkResponse response) {
        List<DocWriteRequest<?>> list = request.requests();
        BulkRequest requestRetry = new BulkRequest();
        //下面尽量控制一下一次bulk的数量，如果数据过大，条数过多可能出现同步不完全的情况
        for (BulkItemResponse bir : response) {
            if (bir.isFailed()) {
                int docIndex = bir.getItemId();
                IndexRequest ir = (IndexRequest) list.get(docIndex);
                requestRetry.add(new IndexRequest("index", "index").source(ir.sourceAsMap(), XContentType.JSON));
            }
        }
        try {
            //遇到错误，休眠1s后重试
            Thread.sleep(1000);
            BulkResponse responseRetry = restHighLevelClient.bulk(requestRetry, RequestOptions.DEFAULT);
            //重试仍然失败时记录该数据
            exceptionLog(requestRetry, responseRetry);
        } catch (Exception e) {
            log.error("ES同步重试出错！", e);
        }
    }

    /**
     * 重试结果判断
     */
    private void exceptionLog(BulkRequest request, BulkResponse response) {
        List<DocWriteRequest<?>> list = request.requests();
        for (BulkItemResponse bir : response) {
            if (bir.isFailed()) {
                int docIndex = bir.getItemId();
                IndexRequest ir = (IndexRequest) list.get(docIndex);
                //记录失败原因及失败数据
                log.error("同步重试失败reason=[{}]，data=[{}]", bir.getFailureMessage(), ir.sourceAsMap().toString());
            }
        }
    }

    @Override
    public List<StudentVO> getList(String indexName, StudentVO query) {
        SearchRequest request = new SearchRequest(indexName);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(makeQueryParams(query));//查询参数
        sourceBuilder.from(query.getOffset()).size(query.getPageSize());//分页
        sourceBuilder.sort("score", SortOrder.DESC);//排序字段

        request.source(sourceBuilder);

        SearchHit[] hits = new SearchHit[0];
        try {
            hits = restHighLevelClient.search(request, RequestOptions.DEFAULT).getHits().getHits();
        } catch (Exception e) {
            log.error("ES查询出错: {}", e.getMessage(), e);
        }

        List<StudentVO> data = new ArrayList<>();
        for (SearchHit hit : hits) {
            data.add(GSON.fromJson(hit.getSourceAsString(), StudentVO.class));
        }
        return data;
    }

    @Override
    public void getHighlightList(String indexName, StudentVO query) {
        Integer pageSize = query.getPageSize();
        Integer offset = query.getOffset();
        //init
        if (pageSize == null) pageSize = 20;
        if (offset <= 0) offset = 0;

        SearchRequest request = new SearchRequest(indexName);

        //设置需要高亮的es字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.field("grade");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(makeQueryParams(query));//查询参数
        sourceBuilder.from(offset).size(pageSize);//分页
        //sourceBuilder.sort("score", SortOrder.DESC);//排序字段
        sourceBuilder.timeout(new TimeValue(1000));
        sourceBuilder.highlighter(highlightBuilder);
        request.source(sourceBuilder);

        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);

            SearchHits searchHits = response.getHits();
            long total = searchHits.getTotalHits();

            log.info("总记录数:" + total);
            log.info("总页码数:" + (total % pageSize == 0 ? total / pageSize : (total / pageSize + 1)));
            log.info("当前偏移:" + offset);

            SearchHit[] hits = searchHits.getHits();
            for (SearchHit hit : hits) {
                Map<String, Object> source = hit.getSourceAsMap();

                log.info("学生ID:" + hit.getId());
                // _score分数为null，打印出来就发现score为NAN。
                // 原因：查询语句searchQuery里面使用了其它字段作为排序字段，es默认使用score为排序字段，如果使用其它字段作为排序字段，_score则为null
                log.info("学生得分:" + hit.getScore());

                //首先从highlight获取es字段，如果命中关键字则有，没有命中直接从Source获取
                if (hit.getHighlightFields().get("name") != null) {
                    Text[] titleTexts = hit.getHighlightFields().get("name").getFragments();
                    StringBuilder buf = new StringBuilder();
                    for (Text text : titleTexts) {
                        buf.append(text);
                    }
                    log.info("学生名称:" + buf);
                } else {
                    log.info("学生名称:" + source.get("name"));
                }

                //获取es字段grade
                if (hit.getHighlightFields().get("grade") != null) {
                    Text[] titleTexts = hit.getHighlightFields().get("grade").getFragments();
                    StringBuilder buf = new StringBuilder();
                    for (Text text : titleTexts) {
                        buf.append(text);
                    }
                    log.info("学生班级:" + buf);
                } else {
                    log.info("学生班级:" + source.get("grade"));
                }
            }
        } catch (Exception e) {
            log.error("ES查询出错: {}", e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getGroupCountByFieldName(String indexName, String fieldName, StudentVO query) {
        // group by count
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms(fieldName + "_count").field(fieldName);

        SearchResponse response = this.aggregationSearch(indexName, aggregationBuilder, query);
        if(response == null) {
            return null;
        }

        Map<String, Object> retMap = Maps.newHashMap();

        ParsedLongTerms agg = response.getAggregations().get(fieldName + "_count");
        for (Terms.Bucket bucket : agg.getBuckets()) {
            retMap.put(bucket.getKeyAsString(), bucket.getDocCount());
        }
        return retMap;
    }

    public Map<String, Object> getGroupCountByFieldNameAndFilter(String indexName, String fieldName, StudentVO query, int c) {
        // 过滤查询（filter）是对集合包含/排除的简单检查，这使得它们计算速度非常快。 当至少有一个过滤查询是“稀疏”（仅有少量匹配的文档）时，
        // 可以利用各种优化，并且可以将缓存经常使用的filter过滤查询缓存在内存中以加快访问速度。

        // 对比之下，query检索(评分查询)不仅要查找匹配的文档，还要计算每个文档的相关程度，这通常会使其比非评分文档更复杂。 另外，查询结果不可缓存。
        // 由于倒排索引，只有几个文档匹配的简单评分查询（query检索）可能会比跨越数百万个文档的过滤器（filter过滤）表现得更好。 但是，一般来说，fiter过滤的性能将胜过评分查询（query检索）。
        // 过滤（filter）的目标是减少必须由评分查询（query）检查的文档数量。

        // Elasticsearch将创建一个文档匹配过滤器的位集bitset（如果文档匹配则为1，否则为0）。 随后用相同的过滤器执行查询将重用此信息。
        // 每当添加或更新新文档时，位集bitset也会更新。

        // 全文检索以及任何使用相关性评分的场景使用query检索
        // 除此之外的其他使用filter过滤器过滤

        // group by count
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms(fieldName + "_count").field(fieldName);

        FilterAggregationBuilder fab = AggregationBuilders
                //.filter(fieldName + "_filter", QueryBuilders.rangeQuery(fieldName + "_count").gt(c))
                .filter(fieldName + "_filter", QueryBuilders.rangeQuery("score").gt(c))
                .subAggregation(aggregationBuilder);

        SearchResponse response = this.aggregationSearch(indexName, fab, query);
        if(response == null) {
            return null;
        }

        Map<String, Object> retMap = Maps.newHashMap();

        ParsedFilter filterAgg = response.getAggregations().get(fieldName + "_filter");
        //log.info(filterAgg.getAggregations().getClass().getName());
        //log.info(filterAgg.getDocCount()+"");
        //for(Map.Entry<String, Aggregation> me : filterAgg.getAggregations().getAsMap().entrySet()) {
        //    log.info("{} = {}", me.getKey(), me.getValue());
        //}

        ParsedLongTerms agg = filterAgg.getAggregations().get(fieldName + "_count");
        //log.info("===" + agg.getBuckets());
        for (Terms.Bucket bucket : agg.getBuckets()) {
            retMap.put(bucket.getKeyAsString(), bucket.getDocCount());
        }
        return retMap;
    }

    @Override
    public Map<String, Object> getGroupCountByFieldNameAndHaving(String indexName, String fieldName, StudentVO query, int c) {
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms(fieldName + "_count").field(fieldName);
        //聚合，count为自带的
        aggregationBuilder.subAggregation(AggregationBuilders.avg(fieldName + "_avg").field(fieldName));

        //声明BucketPath，用于后面的bucket筛选
        Map<String, String> bucketsPathsMap = Maps.newHashMap();
        bucketsPathsMap.put(fieldName + "_count", "_count");
        bucketsPathsMap.put(fieldName + "_avg", fieldName + "_avg");

        //设置脚本
        Script script = new Script("params." + fieldName + "_count" + " >= " + c);

        //构建bucket选择器
        BucketSelectorPipelineAggregationBuilder bs = PipelineAggregatorBuilders.bucketSelector("having", bucketsPathsMap, script);

        aggregationBuilder.subAggregation(bs);

        SearchResponse response = this.aggregationSearch(indexName, aggregationBuilder, query);
        if(response == null) {
            return null;
        }

        Map<String, Object> retMap = Maps.newHashMap();

        ParsedLongTerms agg = response.getAggregations().get(fieldName + "_count");
        for (Terms.Bucket bucket : agg.getBuckets()) {
            retMap.put(bucket.getKeyAsString(), bucket.getDocCount());
        }
        return retMap;
    }

    @Override
    public void getDistinctCountByFieldName(String indexName, String fieldName, StudentVO query) {
        // 去重统计 count(distinct)
        AggregationBuilder aggregationBuilder = AggregationBuilders.cardinality(fieldName + "_count").field(fieldName);

        SearchResponse response = this.aggregationSearch(indexName, aggregationBuilder, query);
        if(response == null) {
            return;
        }

        Cardinality agg = response.getAggregations().get(fieldName + "_count");
        log.info("count = {}", agg.getValue());
    }

    private SearchResponse aggregationSearch(String indexName, AggregationBuilder aggregationBuilder, StudentVO query) {
        SearchRequest request = new SearchRequest(indexName);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(makeQueryParams(query));//查询参数
        sourceBuilder.aggregation(aggregationBuilder);// 分组
        sourceBuilder.from(query.getOffset()).size(query.getPageSize());//分页

        request.source(sourceBuilder);

        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            if (!RestStatus.OK.equals(response.status())) {
                log.warn("ES查询异常，status = {}, msg = {}", response.status(), request.toString());
                return null;
            }
            return response;
        } catch (Exception e) {
            log.error("ES查询出错: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public long count(String indexName, StudentVO student) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //构建判断条件
        sourceBuilder.query(makeQueryParams(student));

        CountRequest countRequest = new CountRequest(indexName);
        countRequest.source(sourceBuilder);
        CountResponse countResponse;
        long count = 0L;
        try {
            countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
            count = countResponse != null ? countResponse.getCount() : 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return count;
    }

    //构建查询参数
    private BoolQueryBuilder makeQueryParams(StudentVO student) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //精确查找
        if (student.getAge() != null) {
            boolQueryBuilder.must(termQuery("age", String.valueOf(student.getAge())));
        }
        //模糊匹配
        if (!StringUtils.isEmpty(student.getName())) {
            boolQueryBuilder.must(QueryBuilders.wildcardQuery("name", "*" + student.getName() + "*"));
        }
        return boolQueryBuilder;
    }

    @Override
    public void updateById(String indexName, StudentVO student, String id) {
        if (log.isDebugEnabled()) {
            log.info("es开始更新数据:{}", GSON.toJson(student));
        }

        try {
            UpdateRequest request = new UpdateRequest(indexName, "index", id).doc(BeanUtilsTool.objectToMap(student));
            UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
            log.info("更新状态：{}", response.getResult());
        } catch (IOException e) {
            log.error("更新写入异常:{}", e.getMessage(), e);
        }
        if (log.isDebugEnabled()) {
            log.info("es更新数据完成");
        }
    }

    @Override
    public void deleteByScore(String indexName, int score) {
        DeleteByQueryRequest delReq = new DeleteByQueryRequest(indexName);
        delReq.setDocTypes("index");
        delReq.setQuery(new TermQueryBuilder("score", String.valueOf(score)));
        try {
            restHighLevelClient.deleteByQuery(delReq, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("ES按条件删除出错: {}", e.getMessage(), e);
        }
    }

}
