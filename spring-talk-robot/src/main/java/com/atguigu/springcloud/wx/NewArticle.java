package com.atguigu.springcloud.wx;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 图文类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class NewArticle implements Serializable {

    private static final long serialVersionUID = 6711831671497059666L;

    /**
     * 标题，不超过128个字节，超过会自动截断
     */
    private String title;

    /**
     * 描述，不超过512个字节，超过会自动截断
     */
    private String description;

    /**
     * 点击后跳转的链接。
     */
    private String url;

    /**
     * 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图1068*455，小图150*150。
     */
    private String picUrl;

    /**
     * 按钮文字，仅在图文数为1条时才生效。 默认为“阅读全文”， 不超过4个文字，超过自动截断。该设置只在企业微信上生效，微工作台（原企业号）上不生效。
     */
    private String btnText;

}
