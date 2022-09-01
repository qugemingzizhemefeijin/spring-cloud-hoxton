package com.atguigu.springcloud.test.parallel;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class CompletableCalculateTest {

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(5);

    private long s = 0;

    @Before
    public void beforeMethod() {
        s = System.currentTimeMillis();
    }

    @After
    public void afterMethod() {
        log.info("exec cost time : {}(MS)", System.currentTimeMillis() - s);
    }

    @Test
    public void getCheapestPlatAndPrice() {
        Future<PriceInfo> tbFuture = threadPool.submit(() -> computeRealPrice(HttpRequestMock.getTBPrice(), HttpRequestMock.getTBDiscounts()));
        Future<PriceInfo> jdFuture = threadPool.submit(() -> computeRealPrice(HttpRequestMock.getJDPrice(), HttpRequestMock.getJDDiscounts()));
        Future<PriceInfo> pxxFuture = threadPool.submit(() -> computeRealPrice(HttpRequestMock.getPXXPrice(), HttpRequestMock.getPXXDiscounts()));

        // 等待所有线程结果都处理完成，然后从结果中计算出最低价
        PriceInfo c = Stream.of(tbFuture, jdFuture, pxxFuture)
                .map(priceResultFuture -> {
                    try {
                        return priceResultFuture.get(10L, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .min(Comparator.comparingInt(PriceInfo::getPrice))
                .orElse(new PriceInfo("无", 0));

        log.info("最优价格{}为: {}元", c.getName(), c.getPrice());
    }

    private PriceInfo computeRealPrice(PriceInfo info, int discount) {
        int c = info.getPrice() / discount;
        log.info("计算{}的最终价格：{}元", info.getName(), c);
        return new PriceInfo(info.getName(), c);
    }

    @Test
    public void getCheapestPlatAndPrice2() {
        // 获取TB最终价格
        CompletableFuture<PriceInfo> tb = CompletableFuture
                .supplyAsync(HttpRequestMock::getTBPrice)
                .thenCombine(CompletableFuture.supplyAsync(HttpRequestMock::getTBDiscounts), this::computeRealPrice);

        // 获取JD最终价格
        CompletableFuture<PriceInfo> jd = CompletableFuture
                .supplyAsync(HttpRequestMock::getJDPrice)
                .thenCombine(CompletableFuture.supplyAsync(HttpRequestMock::getJDDiscounts), this::computeRealPrice);

        // 获取PXX最终价格
        CompletableFuture<PriceInfo> pxx = CompletableFuture
                .supplyAsync(HttpRequestMock::getPXXPrice)
                .thenCombine(CompletableFuture.supplyAsync(HttpRequestMock::getPXXDiscounts), this::computeRealPrice);

        // 排序并获取最低价格
        PriceInfo c = Stream.of(tb, jd, pxx)
                .map(CompletableFuture::join)
                .sorted(Comparator.comparingInt(PriceInfo::getPrice))
                .findFirst()
                .get();

        log.info("最优价格{}为: {}元", c.getName(), c.getPrice());
    }

    @Test
    public void comparePriceInOnePlat1Test() {
        // 非并行列表循环（时间其实会花费很长）
        PriceInfo c = IntStream.range(0, 5).mapToObj(i ->
                CompletableFuture.supplyAsync(HttpRequestMock::getTBPrice)
                .thenCombine(CompletableFuture.supplyAsync(HttpRequestMock::getTBDiscounts), this::computeRealPrice).join())
                .sorted(Comparator.comparingInt(PriceInfo::getPrice))
                .findFirst()
                .get();

        log.info("最优价格{}为: {}元", c.getName(), c.getPrice());
    }

    @Test
    public void comparePriceInOnePlat2Test() {
        // 并行列表循环
        List<CompletableFuture<PriceInfo>> completableFutures = IntStream.range(0, 5).mapToObj(i ->
                CompletableFuture.supplyAsync(HttpRequestMock::getTBPrice)
                        .thenCombine(CompletableFuture.supplyAsync(HttpRequestMock::getTBDiscounts), this::computeRealPrice))
                .collect(Collectors.toList());

        PriceInfo c = completableFutures.stream()
                .map(CompletableFuture::join)
                .sorted(Comparator.comparingInt(PriceInfo::getPrice))
                .findFirst()
                .get();

        log.info("最优价格{}为: {}元", c.getName(), c.getPrice());
    }

}
