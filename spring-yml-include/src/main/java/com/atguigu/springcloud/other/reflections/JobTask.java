package com.atguigu.springcloud.other.reflections;

import java.util.List;

public class JobTask {

    @Job(jobName = "A_Job")
    public void job1(int a) {
        System.out.println("job1");
    }

    @Job(jobName = "B_Job")
    public <T extends Class<?>> void job2(T clazz) {
        System.out.println("job2");
    }

    @Job(jobName = "C_Job")
    public void job3(int a, String b, List<Object> c) {
        System.out.println("job3");
    }

}
