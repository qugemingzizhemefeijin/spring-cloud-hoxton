package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.service.PromoteEdieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/promotion")
public class ShopController {

    @Autowired
    private PromoteEdieService promoteEdieService;

    /**
     * 编辑促销活动
     *
     * @return 结果
     */
    @GetMapping(value = "/ediePromote")
    public void addPromote(String money, String rulename) {
        promoteEdieService.ediePromomteMap(money, rulename);
    }

    /**
     * 购物车
     *
     * @return 返回结果
     */
    @GetMapping(value = "/toShopping")
    public Map<String, Object> toShopping(String money) {
        return promoteEdieService.toShopping(money);
    }

}
