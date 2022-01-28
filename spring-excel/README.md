### alibaba easyexcel 使用案例

- @ExcelProperty：核心注解，value属性可用来设置表头名称，converter属性可以用来设置类型转换器；
- @ColumnWidth：用于设置表格列的宽度；
- @DateTimeFormat：用于设置日期转换格式。
- 在EasyExcel中，如果你想实现枚举类型到字符串的转换（比如gender属性中，0->男，1->女），需要自定义转换器。

### easypoi

- @Excel 作用到filed上面,是对Excel一列的一个描述
- @ExcelCollection 表示一个集合,主要针对一对多的导出,比如一个老师对应多个科目,科目就可以用集合表示
- @ExcelEntity 表示一个继续深入导出的实体,但他没有太多的实际意义,只是告诉系统这个对象里面同样有导出的字段
- @ExcelIgnore 和名字一样表示这个字段被忽略跳过这个导导出
- @ExcelTarget 这个是作用于最外层的对象,描述这个对象的id,以便支持一个对象可以针对不同导出做出不同处理

### 性能对比

导出100w条数据（分100次，每次模拟返回1w条数据，10线程并发3次，20线程并发2次）内存控制在1G。

easyexcel耗时大概在55s左右，poi的耗时在84s左右。cpu耗时差不多。。总体来说easyexcel性能还是稍微强一些。

总体来说EasyExcel，使用还是挺方便的，性能也很优秀。但是比较常见的一对多导出实现比较复杂，而且功能也不如EasyPoi强大。如果你的Excel导出数据量不大的话，可以使用EasyPoi，如果数据量大，比较在意性能的话，还是使用EasyExcel吧。


[easypoi中文文档](http://easypoi.mydoc.io/#category_50222)

[easyexcel中文文档](https://alibaba-easyexcel.github.io/)
