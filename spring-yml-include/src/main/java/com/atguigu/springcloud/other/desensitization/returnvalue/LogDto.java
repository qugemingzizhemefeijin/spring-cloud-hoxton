package com.atguigu.springcloud.other.desensitization.returnvalue;

import cn.hutool.core.util.DesensitizedUtil;
import lombok.Data;

@Data
public class LogDto {

    // 姓名
    private String name;
    // 身份证号
    @JsonEncrypt(beginIdx = 4, endIdx = 10)
    private String idCard;
    // 手机号
    @JsonEncrypt(beginIdx = 3, endIdx = 9)
    private String cellphone;
    // 金额
    private String money;
    // 备注
    @JsonEncrypt(type = 1)
    private String remark;

    public static void main(String[] args) {
        // 邮箱
        String email = DesensitizedUtil.email("11569963ABC@163.com");
        System.out.println(email);
        // 姓名脱敏
        String name = DesensitizedUtil.chineseName("张小明");
        System.out.println(name);
        // 手机号脱敏
        String phone = DesensitizedUtil.mobilePhone("13891119693");
        System.out.println(phone);
        // 前6位和后4位明文显示
        String idCard = DesensitizedUtil.idCardNum("411224198012257571", 6, 4);
        System.out.println(idCard);
    }

}
