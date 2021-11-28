package com.atguigu.drools.ruleExtends;

import com.atguigu.drools.pojo.Person;
import com.atguigu.drools.pojo.School;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class RuleExtends {

    @Test
    public void testExtends() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("isExtends");
        ks.insert(new Person("张小三"));
        ks.insert(new School("一班"));
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

}
