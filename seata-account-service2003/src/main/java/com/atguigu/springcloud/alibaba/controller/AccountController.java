package com.atguigu.springcloud.alibaba.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.springcloud.alibaba.domain.CommonResult;
import com.atguigu.springcloud.alibaba.service.AccountService;

@SuppressWarnings("rawtypes")
@RestController
public class AccountController {

	@Resource
	AccountService accountService;

	/**
	 * 扣减账户余额
	 */
	@RequestMapping("/account/decrease")
	public CommonResult decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money) {
		accountService.decrease(userId, money);
		return new CommonResult(200, "扣减账户余额成功！");
	}
	
}
