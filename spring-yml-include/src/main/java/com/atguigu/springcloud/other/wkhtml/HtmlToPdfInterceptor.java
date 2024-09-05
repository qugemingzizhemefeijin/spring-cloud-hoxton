package com.atguigu.springcloud.other.wkhtml;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * html转pdf 输出日志
 */
@Slf4j
public class HtmlToPdfInterceptor extends Thread {

    private InputStream is;

    public HtmlToPdfInterceptor(InputStream is) {
        this.is = is;
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                log.info("html转pdf进度和信息：{}", line.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
