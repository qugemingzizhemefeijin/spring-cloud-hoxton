package com.atguigu.liteflow.flownode;

import com.alibaba.fastjson.JSONObject;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class FlowExecutorService {

    @Resource
    private FlowExecutor flowExecutor;

    /**
     * 处理 交易完成后任务,异步执行
     */
    @Async(value = "getAsyncExecutor")
    public void handleApp(AppFlowDto flowDto){
        // 使用的规则文件，传递参数，上下文对象
        LiteflowResponse response = flowExecutor.execute2Resp("test_flow", flowDto, AppFlowContext.class);
        // 获取流程执行后的结果
        if (!response.isSuccess()) {
            Exception e = response.getCause();
            log.warn(" error is {}", e.getCause(), e);
        }
        AppFlowContext context = response.getContextBean(AppFlowContext.class);
        log.info("handleApp 执行完成后 context {}", JSONObject.toJSONString(context));
    }

}
