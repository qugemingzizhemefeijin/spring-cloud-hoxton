package com.atguigu.springcloud.test;

import com.atguigu.springcloud.RemoteClientFeignMain8080;
import com.atguigu.springcloud.openfeign.domain.QueryTokenReq;
import com.atguigu.springcloud.openfeign.domain.QueryTokenResp;
import com.atguigu.springcloud.openfeign.rpc.CECTokenRPC;
import com.atguigu.springcloud.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RemoteClientFeignMain8080.class)
@Slf4j
public class CECFeignClientTest {

    @Autowired
    private CECTokenRPC tokenRPC;

    @Test
    public void test(){
        QueryTokenReq r = new QueryTokenReq();
        r.setOperatorID("action");
        r.setOperatorSecret("abc");

        QueryTokenResp resp = tokenRPC.queryToken(r);

        log.info("resp: {}", JsonUtil.toJson(resp));
    }

}
