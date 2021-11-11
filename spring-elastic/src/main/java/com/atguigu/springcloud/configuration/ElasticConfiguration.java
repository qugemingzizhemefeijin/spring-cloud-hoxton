package com.atguigu.springcloud.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

import java.time.Duration;

@Configuration
public class ElasticConfiguration {

    private HttpHost[] getHttpHosts(String clientIps, int esHttpPort) {
        String[] clientIpList = clientIps.split(",");
        HttpHost[] httpHosts = new HttpHost[clientIpList.length];
        for (int i = 0; i < clientIpList.length; i++) {
            httpHosts[i] = new HttpHost(clientIpList[i], esHttpPort, "http");
        }
        return httpHosts;
    }

    // 官方建议我们使用：High Level REST Client
    @Bean
    RestHighLevelClient restHighLevelClient() {
        ClientConfiguration configuration = ClientConfiguration.builder()
                // 这里可以设置多个链接地址
                .connectedToLocalhost()
                //.connectedTo("localhost:9200")
                .withConnectTimeout(Duration.ofSeconds(5))
                .withSocketTimeout(Duration.ofSeconds(3))
                //.useSsl()
                //.withDefaultHeaders(defaultHeaders)
                .withBasicAuth("elastic", "1qazxsw2")
                // ... other options
                .build();

        return RestClients.create(configuration).rest();
    }

}
