package com.atguigu.liteflow.flownode.app;

import com.alibaba.fastjson.JSONObject;

import com.atguigu.liteflow.flownode.AppFlowContext;
import com.atguigu.liteflow.flownode.AppFlowDto;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component(value = "prepareTrade")
public class PrepareTrade extends NodeComponent {

    @Override
    public void process() throws Exception {
        log.info("交易完成后业务处理数据准备和校验");
        //拿到请求参数
        AppFlowDto req = this.getSlot().getRequestData();
        log.info("请求参数 {}", JSONObject.toJSONString(req));
        // 停止任务
        // setIsEnd(Boolean.TRUE);
        AppFlowContext context = this.getContextBean(AppFlowContext.class);
        log.info("设置上下文对象 {}", JSONObject.toJSONString(context));
    }

}
