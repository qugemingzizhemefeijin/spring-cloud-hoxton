package com.atguigu.springcloud.other.shortlink;

import cn.hutool.core.codec.Base64;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ShortLinkGenerator2 {

    private static final String BIT_STR = "0123456789abcdefghjkmnpqrstuwxyzABCDEFGHJKLMNPQRSTUWXYZ";

    public static void main(String[] args) {
        String shortLink = encrypt(123456789L);
        System.out.println(shortLink);
        System.out.println(decrypt(shortLink));
    }

    public static String encrypt(Long id) {
        return Base64.encode(longEncryptToString(id))
                .replace('+', '-')
                .replace('/', '_')
                .replace('=', ' ')
                .trim();
    }

    public static Long decrypt(String s) {
        if (s == null || s.trim().equals("")) {
            return null;
        }
        return stringDecryptToLong(
                Base64.decodeStr(s.replace('-', '+')
                        .replace('_', '/')
                        .replace(' ', '=')));
    }

    private static String longEncryptToString(long v) {
        List<Character> arrays = new ArrayList<>(12);
        for (int i = 11; i > 0; i--) {
            double d = Math.pow(BIT_STR.length(), i);
            if (v >= d) {
                arrays.add(BIT_STR.charAt((int) (v / (long) d)));
                v = v % (long) d;
            } else {
                if (arrays.size() > 0) {
                    arrays.add(BIT_STR.charAt(0));
                }
            }
        }
        arrays.add(BIT_STR.charAt((int) v));

        return StringUtils.join(arrays, "");
    }

    private static long stringDecryptToLong(String s) {
        long num = 0L;
        int bl = BIT_STR.length(), sl = s.length();
        for (int i = sl - 1; i >= 0; i--) {
            int dx = BIT_STR.indexOf(s.charAt(i));
            if (dx == -1) {
                return 0L;
            }
            double d = Math.pow(bl, sl - i - 1);
            num += dx * d;
        }
        return num;
    }

}
