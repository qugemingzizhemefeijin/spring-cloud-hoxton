package com.atguigu.liteflow.config;

import cn.hutool.core.lang.Snowflake;
import com.yomahub.liteflow.flow.id.RequestIdGenerator;
import lombok.Data;

/*
# flow 规则表达式 选择组件
SWITCH(a).to(b, c);
# processWitch 表达式需要返回的是 b 或者 c 字符串来执行相应的业务逻辑
# flow 规则表达式 条件组件
IF(x, a, b);


# 文件编排， then 代表串行执行  when 表示并行执行
# 串行编排示例
THEN(a, b, c, d);
# 并行编排示例
WHEN(a, b, c);
# 串行和并行嵌套结合
THEN( a, WHEN(b, c, d), e);
# 选择编排示例
SWITCH(a).to(b, c, d);
# 条件编排示例
THEN(IF(x, a),b );
 */
@Data
public class AppRequestIdGenerator implements RequestIdGenerator {

    public static final Snowflake flake = new Snowflake();

    @Override
    public String generate() {
        return flake.nextIdStr();
    }

}
