package com.atguigu.springcloud.test.parallel;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public final class HttpRequestMock {

    public static PriceInfo getTBPrice() {
        log.info("获取TB的价格");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("获取TB的价格:{}元", 10);
        return new PriceInfo("TB", 10);
    }

    public static int getTBDiscounts() {
        log.info("获取TB的折扣优惠");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("获取TB的折扣优惠:{}折", 5);
        return 5;
    }

    public static PriceInfo getJDPrice() {
        log.info("获取JD的价格");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("获取JD的价格:{}元", 20);
        return new PriceInfo("JD", 20);
    }

    public static int getJDDiscounts() {
        log.info("获取JD的折扣优惠");
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("获取JD的折扣优惠:{}折", 4);
        return 4;
    }

    public static PriceInfo getPXXPrice() {
        log.info("获取PXX的价格");
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("获取PXX的价格:{}元", 30);
        return new PriceInfo("PXX", 30);
    }

    public static int getPXXDiscounts() {
        log.info("获取PXX的折扣优惠");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("获取PXX的折扣优惠:{}折", 6);
        return 6;
    }

    private HttpRequestMock() {

    }

}
