package com.atguigu.drools.ruleName;

import org.drools.core.base.RuleNameEndsWithAgendaFilter;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.base.RuleNameMatchesAgendaFilter;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

import java.util.HashSet;
import java.util.Set;

public class RuleName {

    @Test
    public void testRuleName() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("isRuleName");
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    // RuleNameEqualsAgendaFilter 根据指定的规则名称过滤
    @Test
    public void testRuleName2() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("isRuleName");
        int count = ks.fireAllRules(new RuleNameEqualsAgendaFilter("指定规则名"));
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    // RuleNameEndsWithAgendaFilter 根据指定的规则名称后缀过滤
    @Test
    public void testRuleName3() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("isRuleName");
        int count = ks.fireAllRules(new RuleNameEndsWithAgendaFilter("三"));
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    // RuleNameStartsWithAgendaFilter 根据指定的规则名称前缀过滤
    @Test
    public void testRuleName4() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("isRuleName");
        int count = ks.fireAllRules(new RuleNameStartsWithAgendaFilter("指定"));
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    // RuleNameMatchesAgendaFilter 使用正则匹配过滤
    @Test
    public void testRuleName5() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("isRuleName");
        int count = ks.fireAllRules(new RuleNameMatchesAgendaFilter("\\w{0,10}"));
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    // CustomAgendaFilter 自定义实现的规则过滤
    @Test
    public void testRuleName6() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("isRuleName");
        Set set=new HashSet();
        set.add("指定规则名");
        set.add("指定规则名四");
        set.add("123");
        int count = ks.fireAllRules(new CustomAgendaFilter(set));
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

}
