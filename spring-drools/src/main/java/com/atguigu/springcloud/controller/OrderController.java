package com.atguigu.springcloud.controller;

import com.atguigu.liteflow.flownode.AppFlowDto;
import com.atguigu.liteflow.flownode.FlowExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class OrderController {

    @Resource
    private FlowExecutorService flowService;

    /**
     * 流程信息
     */
    @GetMapping(value = "flow")
    public String flow(){
        AppFlowDto cxt = new AppFlowDto();
        flowService.handleApp(cxt);

        return "success";
    }

}
