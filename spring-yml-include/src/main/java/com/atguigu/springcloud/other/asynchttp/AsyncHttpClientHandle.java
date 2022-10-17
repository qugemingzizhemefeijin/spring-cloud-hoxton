package com.atguigu.springcloud.other.asynchttp;

import io.netty.handler.codec.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static org.asynchttpclient.Dsl.get;

@Slf4j
public class AsyncHttpClientHandle {

    private final static AsyncHttpClient ASYNC_HTTP_CLIENT = asyncHttpClient();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ASYNC_HTTP_CLIENT.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }));
    }

    public static void main(String[] args) throws Exception {
        test4();
    }

    private static void test4() throws Exception {
        CompletableFuture<Response> whenResponse = ASYNC_HTTP_CLIENT
                .prepareGet("https://cn.bing.com/")
                .execute()
                .toCompletableFuture()
                .exceptionally(e -> {
                    log.error(e.getMessage(), e);

                    // 这里应该可以自定义Response
                    return null;
                })
                .thenApply(response -> {
                    log.info("===================================");
                    return response;
                });
        whenResponse.join(); // wait for completion

        System.out.println("body = " + whenResponse.get().getResponseBody());
    }

    private static void test3() throws Exception {
        Future<Integer> whenStatusCode = ASYNC_HTTP_CLIENT.prepareGet("https://cn.bing.com/").execute(
                new AsyncHandler<Integer>() {
                    private Integer status;

                    @Override
                    public State onStatusReceived(HttpResponseStatus responseStatus) {
                        status = responseStatus.getStatusCode();
                        return State.ABORT;
                    }

                    @Override
                    public State onHeadersReceived(HttpHeaders headers) {
                        return State.ABORT;
                    }

                    @Override
                    public State onBodyPartReceived(HttpResponseBodyPart bodyPart) {
                        return State.ABORT;
                    }

                    @Override
                    public Integer onCompleted() {
                        return status;
                    }

                    @Override
                    public void onThrowable(Throwable t) {
                    }
        });

        Integer statusCode = whenStatusCode.get();

        System.out.println("statusCode = " + statusCode);
    }

    private static void test2() throws Exception {
        Request request = get("https://cn.bing.com/").build();
        Future<Response> whenResponse = ASYNC_HTTP_CLIENT.executeRequest(request);

        System.out.println(whenResponse.get().getResponseBody());
    }

    private static void test1() throws Exception {
        Future<Response> f = ASYNC_HTTP_CLIENT.prepareGet("https://cn.bing.com/").execute();
        Response r = f.get();
        // 模拟其他耗时代码
        Thread.sleep(2000);

        System.out.println(r.getResponseBody());
    }

}
