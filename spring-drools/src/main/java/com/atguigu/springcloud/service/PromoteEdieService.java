package com.atguigu.springcloud.service;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.springcloud.utils.UUIDUtil;
import com.atguigu.springcloud.vo.PromoteExecute;
import com.atguigu.springcloud.vo.RuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PromoteEdieService {

    private Map<String, PromoteExecute> promoteExecuteMap;

    @Autowired
    private PromoteNeaten promoteNeaten;

    /**
     * 生成优惠券
     */
    public void ediePromomteMap(String money, String rulename) {
        if (this.promoteExecuteMap == null) {
            promoteExecuteMap = new HashMap<>();
        }
        PromoteExecute promoteExecute = new PromoteExecute();
        double v = Double.parseDouble(money);
        String rule = UUIDUtil.rule(ruleWorkMap(rulename, v));
        String promoteCode = UUIDUtil.typeJoinTime();
        promoteExecute.setPromoteCode(promoteCode);
        promoteExecute.setWorkContent(rule);
        promoteExecute.setPromoteName(rulename);

        // 这里可以插入数据库，优惠券模板信息

        PromoteExecute execute = promoteNeaten.editRule(rule);
        this.promoteExecuteMap.put(promoteCode, execute);
    }

    /**
     * 组合业务规则Json方法
     *
     * @return 结果
     */
    private String ruleWorkMap(String name, Double money) {
        Map<String, Object> map = new HashMap<>();
        //组合Rule部分
        Map<String, Object> rule = new HashMap<>();
        rule.put("name", name);
        map.put("rule", rule);
        //组合 规则When部分
        Map<String, Object> when = new HashMap<>();
        map.put("condition", when);
        //组合 规则Then部分
        Map<String, Object> then = new HashMap<>();
        then.put("money", money);
        map.put("action", then);
        //组合规则When And Then 部分
        return JSONObject.toJSONString(map);
    }

    /**
     * 购物车计算
     *
     * @return
     */
    public Map<String, Object> toShopping(String moneySum) {
        //购物车请求信息
        Map<String, Object> map = new HashMap<>();
        double v = Double.parseDouble(moneySum);
        List<Object> pn = new ArrayList<>();
        if (this.promoteExecuteMap != null) {
            //证明有优惠券
            for (Map.Entry<String, PromoteExecute> codes : this.promoteExecuteMap.entrySet()) {
                RuleResult ruleResult = DrlExecute.rulePromote(codes.getValue(), v);
                v = ruleResult.getFinallyMoney();
                pn.add(ruleResult);
            }
        }
        map.put("moneySumYuanJia",moneySum);
        map.put("youhuiquanjiegou", pn);
        return map;
    }

}
