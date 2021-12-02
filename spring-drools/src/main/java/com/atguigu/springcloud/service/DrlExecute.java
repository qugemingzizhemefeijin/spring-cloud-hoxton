package com.atguigu.springcloud.service;

import com.atguigu.springcloud.vo.PromoteExecute;
import com.atguigu.springcloud.vo.RuleResult;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.command.Command;
import org.kie.internal.command.CommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DrlExecute {

    /**
     * 判断购物车的所有商品所参加的活动
     *
     * @return 结果
     */
    static RuleResult rulePromote(PromoteExecute promoteExecute, Double moneySum) {
        // 判断业务规则是否存在
        RuleResult ruleresult = new RuleResult();
        //统计所有参活商品的件数和金额
        ruleresult.setMoneySum(moneySum);//返回优惠前的价格
        log.info("优惠前的价格" + moneySum);
        //统计完成后再将参数insert到促销规则中
        List<Command> cmdCondition = new ArrayList<>();
        cmdCondition.add(CommandFactory.newInsert(ruleresult));
        promoteExecute.getWorkSession().execute(CommandFactory.newBatchExecution(cmdCondition));
        log.info("优惠后的价格" + ruleresult.getFinallyMoney());
        return ruleresult;
    }

}
