package com.atguigu.springcloud.other.shortlink;

public class ShortLinkGenerator {

    public static void main(String[] args) {
        long originalId = 123456789L; // 示例原始 long 值
        String shortLink = longToShortLink(originalId);
        System.out.println("Short link: " + shortLink);

        long recoveredId = shortLinkToLong(shortLink);
        System.out.println("Recovered ID: " + recoveredId);
    }

    // 将 long 转换为短链表示的 String
    public static String longToShortLink(long id) {
        final int base = 36; // 使用 36 进制
        StringBuilder result = new StringBuilder();

        while (id > 0) {
            int remainder = (int) (id % base);
            result.insert(0, (char) (remainder + (remainder < 10 ? '0' : 'a' - 10)));
            id /= base;
        }

        return result.toString();
    }

    // 将短链表示的 String 转换回 long
    public static long shortLinkToLong(String shortLink) {
        final int base = 36; // 使用 36 进制
        long result = 0;

        for (char c : shortLink.toCharArray()) {
            result = result * base + (c >= '0' && c <= '9' ? c - '0' : c - 'a' + 10);
        }

        return result;
    }

}
