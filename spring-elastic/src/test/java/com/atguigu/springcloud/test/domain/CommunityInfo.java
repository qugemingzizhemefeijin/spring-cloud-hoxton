package com.atguigu.springcloud.test.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunityInfo implements Serializable {

    private static final long serialVersionUID = 8202393942554988722L;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 区县名称
     */
    private String district;

    /**
     * 小区名称
     */
    private String communityName;

    /**
     * 小区UID
     */
    private String uid;

}
