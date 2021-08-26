package com.atguigu.springcloud.functions;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

@Component
public class DiffListParseFunction implements IParseFunction {

    @Override
    public String functionName() {
        return "DIFF_LIST";
    }

    @SuppressWarnings("unchecked")
    @Override
    public String apply(String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }
        List<String> oldList = (List<String>) LogRecordContext.getVariable("oldList");
        List<String> newList = (List<String>) LogRecordContext.getVariable("newList");
        oldList = oldList == null ? Lists.newArrayList() : oldList;
        newList = newList == null ? Lists.newArrayList() : newList;
        Set<String> deletedSets = Sets.difference(Sets.newHashSet(oldList), Sets.newHashSet(newList));
        Set<String> addSets = Sets.difference(Sets.newHashSet(newList), Sets.newHashSet(oldList));
        StringBuilder stringBuilder = new StringBuilder();
        if (!CollectionUtils.isEmpty(addSets)) {
            stringBuilder.append("新增了 <b>").append(value).append("</b>：");
            for (String item : addSets) {
                stringBuilder.append(item).append("，");
            }
        }
        if (!CollectionUtils.isEmpty(deletedSets)) {
            stringBuilder.append("删除了 <b>").append(value).append("</b>：");
            for (String item : deletedSets) {
                stringBuilder.append(item).append("，");
            }
        }
        return StringUtils.isEmpty(stringBuilder) ? null : stringBuilder.substring(0, stringBuilder.length() - 1);
    }

}
