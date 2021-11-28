package com.atguigu.drools.ruleRHSCommand;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * halt：立即终止规则执行。只有有一条规则执行了drools.halt()，则其他规则将不再进行判断，直接返回结束。
 */
public class RuleRHSCommand {

    @Test
    public void testHalt() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("isRHSCommand");
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }
}
