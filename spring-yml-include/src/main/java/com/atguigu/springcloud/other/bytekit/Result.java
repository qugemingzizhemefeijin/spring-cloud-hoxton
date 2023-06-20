package com.atguigu.springcloud.other.bytekit;

import java.util.Random;

public class Result {

    private Object results;

    public Result(Object results) {
        this.results = results;
    }

    public long getUpdateCount() {
        return new Random().nextInt(200);
    }

    public int getBytesSize() {
        return 1024;
    }

    public String asSql() {
        return "select * from dual";
    }

    public Object getResults() {
        return results;
    }

    public void setResults(Object results) {
        this.results = results;
    }
}
