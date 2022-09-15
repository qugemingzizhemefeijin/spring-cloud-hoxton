package com.atguigu.springcloud.test;

import com.atguigu.springcloud.RemoteRPCMain8080;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RemoteRPCMain8080.class)
@Slf4j
public class CECTest {

//    @Autowired
//    private CECTokenService tokenService;
//
//    @Autowired
//    private CECStationService stationService;
//
//    @Autowired
//    private CECOperatorProperties properties;
//
//    @Test
//    public void test(){
//        QueryTokenReq req = new QueryTokenReq();
//        req.setOperatorID(properties.getOperatorID());
//        req.setOperatorSecret(properties.getOperatorSecret());
//        QueryTokenResp resp = tokenService.queryToken(req);
//        log.info("resp: {}", JsonUtil.toJson(resp));
//    }

}
