package com.atguigu.springcloud.wx;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 微信群机器人消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class WxCpGroupRobotMessage {

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 文本内容，最长不超过2048个字节，markdown内容，最长不超过4096个字节，必须是utf8编码
     * 必填
     */
    private String content;

    /**
     * userid的列表，提醒群中的指定成员(@某个成员)，@all表示提醒所有人，如果开发者获取不到userid，可以使用mentioned_mobile_list
     */
    private List<String> mentionedList;

    /**
     * 手机号列表，提醒手机号对应的群成员(@某个成员)，@all表示提醒所有人
     */
    private List<String> mentionedMobileList;

    /**
     * 图片内容的base64编码
     */
    private String base64;

    /**
     * 图片内容（base64编码前）的md5值
     */
    private String md5;

    /**
     * 图文消息，一个图文消息支持1到8条图文
     */
    private List<NewArticle> articles;

    public String toJson() {
        JSONObject messageJson = new JSONObject().put("msgtype", this.getMsgType());
        switch (this.getMsgType()) {
            case GroupRobotMsgType.TEXT: {
                JSONObject text = new JSONObject();
                JSONArray uidJsonArray = new JSONArray();
                JSONArray mobileJsonArray = new JSONArray();

                text.put("content", this.getContent());

                if (this.getMentionedList() != null) {
                    for (String item : this.getMentionedList()) {
                        uidJsonArray.add(item);
                    }
                }
                if (this.getMentionedMobileList() != null) {
                    for (String item : this.getMentionedMobileList()) {
                        mobileJsonArray.add(item);
                    }
                }
                text.put("mentioned_list", uidJsonArray);
                text.put("mentioned_mobile_list", mobileJsonArray);
                messageJson.put("text", text);
                break;
            }
            case GroupRobotMsgType.MARKDOWN: {
                JSONObject text = new JSONObject().put("content", this.getContent());
                messageJson.put("markdown", text);
                break;
            }
            case GroupRobotMsgType.IMAGE: {
                JSONObject text = new JSONObject()
                        .put("base64", this.getBase64())
                        .put("md5", this.getMd5());
                messageJson.put("image", text);
                break;
            }
            case GroupRobotMsgType.NEWS: {
                JSONObject text = new JSONObject();
                JSONArray array = new JSONArray();
                for (NewArticle article : this.getArticles()) {
                    JSONObject articleJson = new JSONObject()
                            .put("title", article.getTitle())
                            .put("description", article.getDescription())
                            .put("url", article.getUrl())
                            .put("picurl", article.getPicUrl());
                    array.add(articleJson);
                }
                text.put("articles", array);
                messageJson.put("news", text);
                break;
            }
            default:

        }
        return messageJson.toString();
    }

}
