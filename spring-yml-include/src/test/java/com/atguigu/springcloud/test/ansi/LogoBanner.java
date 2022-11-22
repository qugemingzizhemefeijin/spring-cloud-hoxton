package com.atguigu.springcloud.test.ansi;

import com.atguigu.springcloud.test.Test2;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

public class LogoBanner {

    private static final String[] COLORS = {"red", "magenta", "yellow", "green", "magenta", "yellow", "cyan"};

    private static String LOGO = "";

    static {
        try {
            ClassPathResource path = new ClassPathResource("logo.txt");
            String logoText = IOUtils.toString(new FileInputStream(path.getFile()), Charset.defaultCharset());
            logoText = logoText.replaceAll("\r\n", "\n");
            String[] elements = logoText.split("\n");
            int /*高度*/h = 5,/*字符数*/c = 7,/*宽度*/w = 8;
            StringBuilder logoBuilder = new StringBuilder(logoText.length());

            for (int i = 0; i < h; i++) {
                for (int j = 0; j < 7; j++) {
                    String line = elements[j * h + i];
                    logoBuilder.append("@|")
                            .append(COLORS[j])
                            .append(fillWithSpace(line, w))
                            .append("|@");
                }
                logoBuilder.append(Test2.lineSeparator);
            }

            LOGO = logoBuilder.substring(0, logoBuilder.length());

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static String logo() {
        return LOGO;
    }

    public static String welcome() {
        return welcome(Collections.<String, String>emptyMap());
    }

    public static String welcome(Map<String, String> infos) {
        return logo();
    }

    public static String fillWithSpace(String src, int length) {
        if (src.length() >= length) {
            return src;
        }

        char[] result = new char[length];
        char[] srcArray = src.toCharArray();

        System.arraycopy(srcArray, 0, result, 0, srcArray.length);

        int pos = src.length();
        while (pos < length) {
            result[pos++] = ' ';
        }

        return new String(result);

    }

}
