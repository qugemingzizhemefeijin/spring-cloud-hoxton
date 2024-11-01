package com.atguigu.springcloud.other.jdframe;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class WebPvDto {

    private String type;

    private Integer score;

    private Integer pvCount;

    public Object value;

    public WebPvDto(String type, Integer score, Integer pvCount) {
        this.type = type;
        this.score = score;
        this.pvCount = pvCount;
    }

}
