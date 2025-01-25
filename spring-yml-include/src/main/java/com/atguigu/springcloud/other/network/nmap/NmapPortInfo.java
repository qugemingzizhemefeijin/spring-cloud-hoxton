package com.atguigu.springcloud.other.network.nmap;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NmapPortInfo {

    /**
     * 产品
     */
    private String product;

    /**
     * 版本
     */
    private String version;

}
