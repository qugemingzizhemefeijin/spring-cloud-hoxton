package com.atguigu.springcloud.vo;

import java.util.ArrayList;
import java.util.List;

public class RuleResult {

    //参活商品优惠后的价格
    private double finallyMoney;
    //参加活动的名称
    private double moneySum;

    public double getMoneySum() {
        return moneySum;
    }

    public void setMoneySum(double moneySum) {
        this.moneySum = moneySum;
    }

    private List<String> promoteName = new ArrayList<>();


    public double getFinallyMoney() {
        return finallyMoney;
    }

    public void setFinallyMoney(double finallyMoney) {
        this.finallyMoney = finallyMoney;
    }

    public List<String> getPromoteName() {
        return promoteName;
    }

    public void setPromoteName(String promoteName) {
        this.promoteName.add(promoteName);
    }

}
