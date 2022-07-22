package com.atguigu.springcloud.webflux.dao.entity;

import com.atguigu.springcloud.webflux.dao.entity.enums.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 905626961924507351L;

    @Id
    private Long id;

    @Field("user_name")
    private String userName;

    private String note;

    private SexEnum sex;

}
