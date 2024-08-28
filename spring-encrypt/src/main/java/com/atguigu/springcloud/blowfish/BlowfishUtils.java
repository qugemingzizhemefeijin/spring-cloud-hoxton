package com.atguigu.springcloud.blowfish;

import javax.crypto.Cipher;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Security;
import java.util.Base64;

public final class BlowfishUtils {

    public static void main(String[] args) throws Exception {
        System.out.println("encrypt:" + encrypt("1223412"));
        System.out.println("decrypt:" + decrypt(encrypt("1223412")));
    }

    public static String encrypt(String data) throws Exception {
        String s = Base64.getEncoder().encodeToString(encrypt(data.getBytes(StandardCharsets.UTF_8)));
        System.out.println(s);
        return s.replace('+', '-').replace('/', '_').replace('=', ' ').trim();
    }

    public static String decrypt(String data) throws IOException, Exception {
        String s = data.replace('-', '+').replace('_', '/').replace(' ', '=');
        byte[] buf = Base64.getDecoder().decode(s);
        byte[] bt = decrypt(buf);
        return new String(bt);
    }

    private static Key getKey() {
        byte[] arrBTmp = AES_KEY.getBytes();
        //创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[56];
        //将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        //生成密钥
        return new javax.crypto.spec.SecretKeySpec(arrB, Algorithm);
    }

    private static byte[] encrypt(byte[] data) throws Exception {
        Cipher encryptCipher = Cipher.getInstance(Algorithm);
        encryptCipher.init(Cipher.ENCRYPT_MODE, getKey());
        return encryptCipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data) throws Exception {
        Cipher decryptCipher = Cipher.getInstance(Algorithm);
        decryptCipher.init(Cipher.DECRYPT_MODE, getKey());
        return decryptCipher.doFinal(data);
    }

    private final static String AES_KEY = "!@#@#!1238857556564fgk@GHyrfdswasd";
    private final static String Algorithm = "Blowfish";

    static {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
    }

}
