springboot中es的使用（版本号6.8.5）

[Window下载地址](https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.8.5.zip)

[Linux下载地址](https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.8.5.tar.gz)

#### 聚合小案例（版本比较老，部分不适合6.8.5）

ES里面所有的聚合实例都由AggregationBuilders类提供静态方法构造，我们先看下常用有哪些方法使用：
```
//（1）统计某个字段的数量
ValueCountBuilder vcb=  AggregationBuilders.count("count_uid").field("uid");

//（2）去重统计某个字段的数量（有少量误差）
CardinalityBuilder cb= AggregationBuilders.cardinality("distinct_count_uid").field("uid");

//（3）聚合过滤
FilterAggregationBuilder fab= AggregationBuilders.filter("uid_filter").filter(QueryBuilders.queryStringQuery("uid:001"));

//（4）按某个字段分组
TermsBuilder tb=  AggregationBuilders.terms("group_name").field("name");

//（5）求和
SumBuilder  sumBuilder=	AggregationBuilders.sum("sum_price").field("price");

//（6）求平均
AvgBuilder ab= AggregationBuilders.avg("avg_price").field("price");

//（7）求最大值
MaxBuilder mb= AggregationBuilders.max("max_price").field("price"); 

//（8）求最小值
MinBuilder min=	AggregationBuilders.min("min_price").field("price");

//（9）按日期间隔分组
DateHistogramBuilder dhb= AggregationBuilders.dateHistogram("dh").field("date");

//（10）获取聚合里面的结果
TopHitsBuilder thb=  AggregationBuilders.topHits("top_result");

//（11）嵌套的聚合
NestedBuilder nb= AggregationBuilders.nested("negsted_path").path("quests");

//（12）反转嵌套
AggregationBuilders.reverseNested("res_negsted").path("kps");
```

#### ElasticSearch 6.8.5 安装并配置基本安全（密码）

在`elasticsearch.yml`配置文件中添加`xpack`配置:
```
http.cors.enabled: true
http.cors.allow-origin: "*"
http.cors.allow-headers: Authorization
xpack.security.enabled: true
xpack.security.transport.ssl.enabled: true
 
#以下这个可以在浏览器打开测试
#xpack.license.self_generated.type: basic
```

**保存文件后，需要重新启动es，必须！**

进入bin目录，执行设置密码命令，如下：
```
elasticsearch-setup-passwords interactive
```

指令交互过程中，会让设置N个用户的密码，设置完即可。

如果在yml配置文件中，加上以下配置可以在浏览器中提示，输入用户密码。

#### turf.js 地理空间分析库，处理各种地图算法

[turf.js中文网](https://turfjs.fenxianglu.cn/)

[几何计算-基于Turf.js实现多边形的拆分及合并](https://zhuanlan.zhihu.com/p/389395519)

