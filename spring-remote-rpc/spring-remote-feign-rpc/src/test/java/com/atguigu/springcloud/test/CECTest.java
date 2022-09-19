package com.atguigu.springcloud.test;

import com.atguigu.springcloud.RemoteRPCFeignMain8090;
import com.atguigu.springcloud.openfeign.decrypt.CECDecoder;
import com.atguigu.springcloud.openfeign.decrypt.CECEncoder;
import com.atguigu.springcloud.openfeign.domain.CECRequest;
import com.atguigu.springcloud.openfeign.domain.CECResponse;
import com.atguigu.springcloud.openfeign.domain.QueryTokenReq;
import com.atguigu.springcloud.openfeign.service.CECTokenService;
import com.atguigu.springcloud.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RemoteRPCFeignMain8090.class)
@Slf4j
public class CECTest {

    @Resource
    private CECTokenService tokenService;

    @Resource
    private CECDecoder decoder;

    @Resource
    private CECEncoder encoder;

    // req : {"OperatorID":"12345678","Data":"wmOCQxkCPVDZxG6d3CLfdKkfWOthZnk2v7praKjF4bgDiDg9ag154fK+6pabnyu6","TimeStamp":"20220916202844","Seq":"0001","Sig":"3F6BD7E1D67BEF10B4A6FDCD3148F7BA"}
    // res: {"data":"T9de6ROhrrUOqpWI2gKtq0ACdoyEMm1AFVTzd9+mqHdiSH86vnhqdEjOtPJ1flHn","ret":0,"msg":"success"}

    @Test
    public void test(){
        QueryTokenReq r = new QueryTokenReq();
        r.setOperatorID("action");
        r.setOperatorSecret("abc");

        CECRequest<String> req = encoder.encrypt(r);
        log.info("req : {}", JsonUtil.toJson(req));

        String ret = encoder.getEncrypt(tokenService.queryToken(r));

        CECResponse<String> vvv = new CECResponse<>();
        vvv.setData(ret);
        vvv.setMsg("success");
        vvv.setRet(0);

        log.info("res: {}", JsonUtil.toJson(vvv));
    }

}
