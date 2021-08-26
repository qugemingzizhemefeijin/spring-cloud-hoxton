Springboot - 注解 - 操作日志

https://mp.weixin.qq.com/s/szhCq0tLCb_870uqAyIwPg

git clone git@github.com:mouzt/mzt-biz-log.git

```
<dependency>
    <groupId>io.github.mouzt</groupId>
    <artifactId>bizlog-sdk</artifactId>
    <version>1.0.4</version>
</dependency>
```

代码：
```
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableTransactionManagement
@EnableLogRecord(tenant = "com.mzt.test")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
```