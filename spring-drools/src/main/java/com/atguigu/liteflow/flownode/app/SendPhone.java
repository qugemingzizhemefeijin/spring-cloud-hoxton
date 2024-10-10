package com.atguigu.liteflow.flownode.app;

import cn.hutool.core.util.RandomUtil;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component(value = "sendPhone")
public class SendPhone extends NodeComponent {
    @Override
    public void process() throws Exception {
        TimeUnit.SECONDS.sleep(RandomUtil.randomInt(0, 20));
        log.info("handle send phone !");
    }
}
