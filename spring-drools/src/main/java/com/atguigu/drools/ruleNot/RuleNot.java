package com.atguigu.drools.ruleNot;

import com.atguigu.drools.pojo.Person;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class RuleNot {

    public static void main(String[] args) {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("isNot");
        Person person2 = new Person();
        person2.setClassName("四班");
        ks.insert(person2);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

}
