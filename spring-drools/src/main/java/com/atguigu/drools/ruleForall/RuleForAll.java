package com.atguigu.drools.ruleForall;

import com.atguigu.drools.pojo.Person;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class RuleForAll {

    public static void main(String[] args) {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("isForall");
        Person person = new Person();
        person.setAge(30);
        person.setName("张三");
        ks.insert(person);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

}
