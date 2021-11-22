package com.atguigu.springcloud.test.junit.params;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(JUnitParamsRunner.class)
@Slf4j
public class JUnitParamsTest {

    /**
     * 比较用户名年龄
     * @param name 名称
     * @param age  年级
     * @param pass 是否及格
     * @throws Exception
     */
    @Test
    @Parameters
    @TestCaseName("{0}, {1}, {2}")
    public void scoreTest(String name, int age, boolean pass) throws Exception {
        log.info("scoreTest name={}, age={}, pass={}", name, age, pass);
    }

    private Object[] parametersForScoreTest() {
        return new Object[]{
                new Object[] {"小明", 18, false},
                new Object[] {"小红", 19, true},
                new Object[] {"小王", 17, false},
                new Object[] {"小芳", 18, true}
        };
    }

}
