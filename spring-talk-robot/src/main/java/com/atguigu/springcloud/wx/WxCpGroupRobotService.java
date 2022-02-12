package com.atguigu.springcloud.wx;

import java.util.List;

/**
 * 微信群机器人消息发送api
 */
public interface WxCpGroupRobotService {

    /**
     * 发送text类型的消息
     *
     * @param content       文本内容，最长不超过2048个字节，必须是utf8编码
     * @param mentionedList userId的列表，提醒群中的指定成员(@某个成员)，@all表示提醒所有人，如果开发者获取不到userId，可以使用mentioned_mobile_list
     * @param mobileList    手机号列表，提醒手机号对应的群成员(@某个成员)，@all表示提醒所有人
     */
    void sendText(String content, List<String> mentionedList, List<String> mobileList);

    /**
     * 发送markdown类型的消息
     *
     * @param content markdown内容，最长不超过4096个字节，必须是utf8编码
     */
    void sendMarkdown(String content);

    /**
     * 发送image类型的消息
     *
     * @param base64 图片内容的base64编码
     * @param md5    图片内容（base64编码前）的md5值
     */
    void sendImage(String base64, String md5);

    /**
     * 发送news类型的消息
     *
     * @param articleList 图文消息，支持1到8条图文
     */
    void sendNews(List<NewArticle> articleList);

    /**
     * 发送text类型的消息
     *
     * @param webhookUrl    webhook地址
     * @param content       文本内容，最长不超过2048个字节，必须是utf8编码
     * @param mentionedList userId的列表，提醒群中的指定成员(@某个成员)，@all表示提醒所有人，如果开发者获取不到userId，可以使用mentioned_mobile_list
     * @param mobileList    手机号列表，提醒手机号对应的群成员(@某个成员)，@all表示提醒所有人
     */
    void sendText(String webhookUrl, String content, List<String> mentionedList, List<String> mobileList);

    /**
     * 发送markdown类型的消息
     *
     * @param webhookUrl webhook地址
     * @param content    markdown内容，最长不超过4096个字节，必须是utf8编码
     */
    void sendMarkdown(String webhookUrl, String content);

    /**
     * 发送image类型的消息
     *
     * @param webhookUrl webhook地址
     * @param base64     图片内容的base64编码
     * @param md5        图片内容（base64编码前）的md5值
     */
    void sendImage(String webhookUrl, String base64, String md5);

    /**
     * 发送news类型的消息
     *
     * @param webhookUrl  webhook地址
     * @param articleList 图文消息，支持1到8条图文
     */
    void sendNews(String webhookUrl, List<NewArticle> articleList);

}
