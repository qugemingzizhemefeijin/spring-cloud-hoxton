package com.atguigu.springcloud.jasypt;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class JasyptUtil {

    public static void main(String[] args) {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        /*配置文件中配置如下的算法*/
        standardPBEStringEncryptor.setAlgorithm("PBEWithMD5AndDES");
        /*配置文件中配置的password*/
        standardPBEStringEncryptor.setPassword("0f7b0a5d-46bc-40fd-b8ed-3181d21d644f");
        //加密
        String jasyptPasswordEN = standardPBEStringEncryptor.encrypt("xj2022");
        //解密
        String jasyptPasswordDE = standardPBEStringEncryptor.decrypt(jasyptPasswordEN);
        System.out.println("加密后密码：" + jasyptPasswordEN);
        System.out.println("解密后密码：" + jasyptPasswordDE);
    }

}
