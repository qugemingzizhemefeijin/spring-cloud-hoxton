### 企业微信机器人

#### 文本类型

```
{
"msgtype": "text",
"text": {
    "content": "广州今日天气：29度，大部分多云，降雨概率：60%",
    "mentioned_list":["wangqing","@all"],
    "mentioned_mobile_list":["13800001111","@all"]
}
}
```

| 参数 | 必须 | 说明 |
| --- | --- | --- |
| msgtype | true | 消息类型，此时固定为text |
| content | true | 文本内容，最长不超过2048个字节，必须是utf8编码 |
| mentioned_list | false | userid的列表，提醒群中的指定成员(@某个成员)，@all表示提醒所有人，如果开发者获取不到userid，可以使用mentioned_mobile_list |
| mentioned_mobile_list | false | 手机号列表，提醒手机号对应的群成员(@某个成员)，@all表示提醒所有人 |

#### markdown类型

```
{
"msgtype": "markdown",
"markdown": {
    "content": "实时新增用户反馈<font color=\"warning\">132例</font>，请相关同事注意。\n
    >类型:<font color=\"comment\">用户反馈</font> \n
    >普通用户反馈:<font color=\"comment\">117例</font> \n
    >VIP用户反馈:<font color=\"comment\">15例</font>"
}
}
```

| 参数 | 必须 | 说明 |
| --- | --- | --- |
| msgtype | true | 消息类型，此时固定为markdown |
| content | true | markdown内容，最长不超过4096个字节，必须是utf8编码 |

#### 图片类型

```
{
"msgtype": "image",
"image": {
    "base64": "DATA",
    "md5": "MD5"
}
}
```

| 参数 | 必须 | 说明 |
| --- | --- | --- |
| msgtype | true | 消息类型，此时固定为image |
| base64 | true | 图片内容的base64编码 |
| md5 | true | 图片内容（base64编码前）的md5值 |

> 注：图片（base64编码前）最大不能超过2M，支持JPG,PNG格式

#### 图文类型

```
{
"msgtype": "news",
"news": {
    "articles" : [
    {
    "title" : "中秋节礼品领取",
    "description" : "今年中秋节公司有豪礼相送",
    "url" : "URL",
    "picurl" : "http://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png"
    }
    ]
}
}
```

| 参数 | 必须 | 说明 |
| --- | --- | --- |
| msgtype | true | 消息类型，此时固定为news |
| articles | 是 | 图文消息，一个图文消息支持1到8条图文 |
| title | 是 | 标题，不超过128个字节，超过会自动截断 |
| description | 否 | 描述，不超过512个字节，超过会自动截断 |
| url | 是 | 点击后跳转的链接。 |
| picurl | 否 | 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图 1068455，小图150150。 |

[参考官方文档](https://work.weixin.qq.com/help?person_id=1&doc_id=13376&from=search&helpType=)

### 钉钉机器人

[参考官方文档](https://open.dingtalk.com/document/group/custom-robot-access)
