package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.poi.ExcelColumnHead;
import com.atguigu.springcloud.poi.ExcelView;
import com.atguigu.springcloud.poi.domain.Member;
import com.atguigu.springcloud.utils.LocalJsonUtil;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@Api(tags = "PoiController", description = "poi导入导出测试")
@RequestMapping("/poi")
@Slf4j
public class PoiController {

    @ApiOperation(value = "导出会员列表Excel")
    @RequestMapping(value = "/exportMemberList", method = RequestMethod.GET)
    public View exportMemberList(HttpServletResponse response) {
        List<ExcelColumnHead<Member , Object>> headList = Lists.newArrayList();
        headList.add(ExcelColumnHead.builder("id", Member::getId));
        headList.add(ExcelColumnHead.builder("用户名", Member::getUserName));
        headList.add(ExcelColumnHead.builder("昵称", Member::getNickName));
        headList.add(ExcelColumnHead.builder("出生日期", Member::getBirthday));
        headList.add(ExcelColumnHead.builder("手机号", Member::getPhone));
        headList.add(ExcelColumnHead.builder("性别", Member::getGender));

        List<Member> memberList = LocalJsonUtil.getListFromJson("json/members.json", Member.class);

        return new ExcelView<>("会员列表", headList, memberList);
    }

}
