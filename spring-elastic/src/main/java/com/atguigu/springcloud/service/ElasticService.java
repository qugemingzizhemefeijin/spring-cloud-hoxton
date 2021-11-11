package com.atguigu.springcloud.service;

import com.atguigu.springcloud.vo.StudentVO;

import java.util.List;
import java.util.Map;

public interface ElasticService {

    void helloWorld();

    void createIndex(String indexName, List<StudentVO> studentList);

    List<StudentVO> getList(String indexName, StudentVO query);

    void getHighlightList(String indexName, StudentVO query);

    Map<String, Object> getGroupCountByFieldName(String indexName, String fieldName, StudentVO query);

    Map<String, Object> getGroupCountByFieldNameAndFilter(String indexName, String fieldName, StudentVO query, int c);

    Map<String, Object> getGroupCountByFieldNameAndHaving(String indexName, String fieldName, StudentVO query, int c);

    void getDistinctCountByFieldName(String indexName, String fieldName, StudentVO query);

    long count(String indexName, StudentVO student);

    void updateById(String indexName, StudentVO student, String id);

    void deleteByScore(String indexName, int score);

}
