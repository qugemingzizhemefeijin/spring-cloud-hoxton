package com.atguigu.springcloud.test.retrofit;

import com.atguigu.springcloud.other.http.retrofit.ComfyuiApi;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ComfyuiApiTest {

    @Autowired
    private ComfyuiApi comfyuiApi;

    @Test
    public void test() throws Exception {
        System.out.println(comfyuiApi.getSystemStats().execute().body());
    }

}
