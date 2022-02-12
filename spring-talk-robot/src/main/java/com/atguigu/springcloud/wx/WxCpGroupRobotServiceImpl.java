package com.atguigu.springcloud.wx;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import lombok.SneakyThrows;

import java.util.List;

/**
 * 企业微信群机器人消息发送API实现
 */
public class WxCpGroupRobotServiceImpl implements WxCpGroupRobotService {

    public static final String CP_BASE_URL = "https://qyapi.weixin.qq.com";
    public static final String WEBHOOK_SEND = "/cgi-bin/webhook/send?key=";

    public final String webhookKey;

    @SneakyThrows
    public WxCpGroupRobotServiceImpl(String webhookKey) {
        if (StrUtil.isBlank(webhookKey)) {
            throw new WxErrorException("无效的企业微信群机器人KEY");
        }
        this.webhookKey = webhookKey;
    }

    private String getWebhookUrl() {
        return CP_BASE_URL + WEBHOOK_SEND + webhookKey;
    }

    @Override
    public void sendText(String content, List<String> mentionedList, List<String> mobileList) {
        this.sendText(this.getWebhookUrl(), content, mentionedList, mobileList);
    }

    @Override
    public void sendMarkdown(String content) {
        this.sendMarkdown(this.getWebhookUrl(), content);
    }

    @Override
    public void sendImage(String base64, String md5) {
        this.sendImage(this.getWebhookUrl(), base64, md5);
    }

    @Override
    public void sendNews(List<NewArticle> articleList) {
        this.sendNews(this.getWebhookUrl(), articleList);
    }

    @Override
    public void sendText(String webhookUrl, String content, List<String> mentionedList, List<String> mobileList) {
        HttpUtil.post(webhookUrl, new WxCpGroupRobotMessage()
                .setMsgType(GroupRobotMsgType.TEXT)
                .setContent(content)
                .setMentionedList(mentionedList)
                .setMentionedMobileList(mobileList)
                .toJson());
    }

    @Override
    public void sendMarkdown(String webhookUrl, String content) {
        HttpUtil.post(webhookUrl, new WxCpGroupRobotMessage()
                .setMsgType(GroupRobotMsgType.MARKDOWN)
                .setContent(content)
                .toJson());
    }

    @Override
    public void sendImage(String webhookUrl, String base64, String md5) {
        HttpUtil.post(this.getWebhookUrl(), new WxCpGroupRobotMessage()
                .setMsgType(GroupRobotMsgType.IMAGE)
                .setBase64(base64)
                .setMd5(md5).toJson());
    }

    @Override
    public void sendNews(String webhookUrl, List<NewArticle> articleList) {
        HttpUtil.post(this.getWebhookUrl(), new WxCpGroupRobotMessage()
                .setMsgType(GroupRobotMsgType.NEWS)
                .setArticles(articleList).toJson());
    }

}
