package com.atguigu.springcloud.test;

import com.atguigu.springcloud.ElasticMain8080;
import com.atguigu.springcloud.service.ElasticService;
import com.atguigu.springcloud.vo.StudentVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticMain8080.class)
@Slf4j
public class ElasticServiceTest {

    private final String indexName = "classes";

    @Autowired
    private ElasticService elasticService;

    // http://localhost:9200/classes/_search
    @Test
    public void createIndexTest() {
        List<StudentVO> studentVOList = Lists.newArrayList();

        String[] names = new String[]{"仙路物语", "小橙子", "孙悟空", "亡者农药", "老橙子", "万岁爷"};
        for(int i=200;i<3000;i++) {
            StudentVO s = new StudentVO();
            s.setName(names[ThreadLocalRandom.current().nextInt(names.length)] + i);
            s.setAge(ThreadLocalRandom.current().nextInt(2) + 10);
            s.setSex(ThreadLocalRandom.current().nextInt(2) == 1 ? "女" : "男");
            s.setGrade("一年级");
            s.setSubject("数学");
            s.setScore(ThreadLocalRandom.current().nextInt(10) + 90);
            studentVOList.add(s);
        }

        // 创建索引
        elasticService.createIndex(indexName, studentVOList);

    }

    @Test
    public void countTest() {
        StudentVO s = new StudentVO();
        s.setName("橙");
        s.setAge(18);

        // 查询数量
        log.info("count = {}", elasticService.count(indexName, s));
    }

    @Test
    public void getListTest() {
        StudentVO s = new StudentVO();
        s.setPageSize(10);
        s.setOffset(0);
        s.setName("橙");

        List<StudentVO> studentList = elasticService.getList(indexName, s);
        if(studentList == null || studentList.isEmpty()) {
            log.info("studentList is empty");
            return;
        }

        studentList.forEach(System.out::println);
    }

    @Test
    public void getHighlightListTest() {
        StudentVO s = new StudentVO();
        s.setPageSize(10);
        s.setOffset(0);
        s.setName("橙");

        elasticService.getHighlightList(indexName, s);
    }

    @Test
    public void getGroupCountByFieldNameTest() {
        StudentVO s = new StudentVO();
        s.setPageSize(10);
        s.setOffset(0);
        s.setName("橙");

        Map<String, Object> retMap = elasticService.getGroupCountByFieldName(indexName, "age", s);
        if(MapUtils.isEmpty(retMap)) {
            log.info("未查询到符合条件的数据");
            return;
        }

        for(Map.Entry<String, Object> me : retMap.entrySet()) {
            log.info("年龄为：{}的学生有{}个", me.getKey(), me.getValue());
        }
    }

    @Test
    public void getGroupCountByFieldNameAndFilterTest() {
        StudentVO s = new StudentVO();
        s.setPageSize(10);
        s.setOffset(0);
        s.setName("橙");

        Map<String, Object> retMap = elasticService.getGroupCountByFieldNameAndFilter(indexName, "score", s, 94);
        if(MapUtils.isEmpty(retMap)) {
            log.info("未查询到符合条件的数据");
            return;
        }

        for(Map.Entry<String, Object> me : retMap.entrySet()) {
            log.info("分数为：{}的学生有{}个", me.getKey(), me.getValue());
        }
    }

    @Test
    public void getGroupCountByFieldNameAndHavingTest() {
        StudentVO s = new StudentVO();
        s.setPageSize(10);
        s.setOffset(0);
        s.setName("橙");

        Map<String, Object> retMap = elasticService.getGroupCountByFieldNameAndHaving(indexName, "score", s, 91);
        if(MapUtils.isEmpty(retMap)) {
            log.info("未查询到符合条件的数据");
            return;
        }

        for(Map.Entry<String, Object> me : retMap.entrySet()) {
            log.info("分数为：{}的学生有{}个", me.getKey(), me.getValue());
        }
    }

    @Test
    public void getDistinctCountByFieldNameTest() {
        StudentVO s = new StudentVO();
        s.setPageSize(10);
        s.setOffset(0);
        s.setName("橙");

        elasticService.getDistinctCountByFieldName(indexName, "age", s);
    }

    @Test
    public void updateTest() {
        StudentVO s = new StudentVO();
        s.setName("老橙子");
        s.setAge(38);
        s.setSex("男");
        s.setGrade("大专");
        s.setSubject("地理");
        s.setScore(116);

        String id = "bJrVDX0BRH8TXQziV_Mp";

        elasticService.updateById(indexName, s, id);
    }

    @Test
    public void deleteByScoreTest() {
        elasticService.deleteByScore(indexName, 88);
    }

}
