package com.atguigu.springcloud.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.atguigu.springcloud.easypoi.MemberExcelDataHandler;
import com.atguigu.springcloud.easypoi.domain.Member;
import com.atguigu.springcloud.easypoi.domain.Order;
import com.atguigu.springcloud.easypoi.domain.Product;
import com.atguigu.springcloud.utils.CommonResult;
import com.atguigu.springcloud.utils.LocalJsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * EasyPoi导入导出测试Controller
 */
@Controller
@Api(tags = "EasyPoiController", description = "EasyPoi导入导出测试")
@RequestMapping("/easyPoi")
@Slf4j
public class EasyPoiController {

    @ApiOperation(value = "导出会员列表Excel")
    @RequestMapping(value = "/exportMemberList", method = RequestMethod.GET)
    public void exportMemberList(ModelMap map,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        List<Member> memberList = LocalJsonUtil.getListFromJson("json/members.json", Member.class);
        ExportParams params = new ExportParams("会员列表", "会员列表", ExcelType.XSSF);
        //对导出结果进行自定义处理
        MemberExcelDataHandler handler = new MemberExcelDataHandler();
        handler.setNeedHandlerFields(new String[]{"昵称"});
        params.setDataHandler(handler);

        map.put(NormalExcelConstants.DATA_LIST, memberList);
        map.put(NormalExcelConstants.CLASS, Member.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "memberList");
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation("从Excel导入会员列表")
    @RequestMapping(value = "/importMemberList", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult importMemberList(@RequestPart("file") MultipartFile file) {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        try {
            List<Member> list = ExcelImportUtil.importExcel(file.getInputStream(), Member.class, params);
            return CommonResult.success(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return CommonResult.failed("导入失败！");
        }
    }

    @ApiOperation(value = "导出订单列表Excel")
    @RequestMapping(value = "/exportOrderList", method = RequestMethod.GET)
    public void exportOrderList(ModelMap map,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        List<Order> orderList = getOrderList();
        ExportParams params = new ExportParams("订单列表", "订单列表", ExcelType.XSSF);
        //导出时排除一些字段
        params.setExclusions(new String[]{"ID", "出生日期", "性别"});
        map.put(NormalExcelConstants.DATA_LIST, orderList);
        map.put(NormalExcelConstants.CLASS, Order.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "orderList");
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    private List<Order> getOrderList() {
        List<Order> orderList = LocalJsonUtil.getListFromJson("json/orders.json", Order.class);
        List<Product> productList = LocalJsonUtil.getListFromJson("json/products.json", Product.class);
        List<Member> memberList = LocalJsonUtil.getListFromJson("json/members.json", Member.class);
        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            order.setMember(memberList.get(i));
            order.setProductList(productList);
        }
        return orderList;
    }
}
