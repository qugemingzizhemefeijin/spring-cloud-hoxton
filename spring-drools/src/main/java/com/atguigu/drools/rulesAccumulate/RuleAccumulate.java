package com.atguigu.drools.rulesAccumulate;

import com.atguigu.drools.pojo.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * accumulate的使用，有一个很重要的action，这个函数提供了匹配源模式的执行动作
 *
 * 将action看成两个状态，当源对象匹配源模式时，定会触发action，将触发过action的源对象称为有状态的，反之为无状态。
 *
 * 当传入源对象在RHS中或其他规则的RHS中发生改变时，所有满足条件规则的将会再次被激活。
 *
 * 当规则再次被执行且遇到accumulate时，则偶状态的源对象会先执行reverse函数进行“回滚”操作，并将修改后的源对象再次与accmulate条件进行比较，
 * 若比较为true，则该对象会再次触发action函数；若比较为false，则不会执行acton函数。
 *
 */
public class RuleAccumulate {

    @Test
    public void testAccumulate(){
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks =kc.newKieSession("isAccumulate");
        Person person=new Person();person.setName("张三");person.setAge(50);  ks.insert(person);
        Person person2=new Person();person2.setName("李四");person2.setAge(20);  ks.insert(person2);
        Person person3=new Person();person3.setName("王五");person3.setAge(25);  ks.insert(person3);
        Person person4=new Person();person4.setName("赵六");person4.setAge(15);  ks.insert(person4);
        int count = ks.fireAllRules();
        System.out.println("总执行了"+count+"条规则");
        ks.dispose();
    }

    @Test
    public void testAccumulate2(){
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks =kc.newKieSession("isAccumulate");
        Person person=new Person();person.setName("张三");person.setAge(50);person.setDous(5.0);  ks.insert(person);
        Person person2=new Person();person2.setName("李四");person2.setAge(20);  ks.insert(person2);
        Person person3=new Person();person3.setName("王五");person3.setAge(25);  ks.insert(person3);
        Person person4=new Person();person4.setName("赵六");person4.setAge(15);  ks.insert(person4);
        int count = ks.fireAllRules();
        System.out.println("总执行了"+count+"条规则");
        ks.dispose();
    }

    @Test
    public void testAccumulate3(){
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("customize");
        Person person=new Person();person.setName("张三");person.setAge(1);  ks.insert(person);
        Person person2=new Person();person2.setName("李四");person2.setAge(2);  ks.insert(person2);
        Person person3=new Person();person3.setName("王五");person3.setAge(3);  ks.insert(person3);
        Person person4=new Person();person4.setName("赵六");person4.setAge(4);  ks.insert(person4);
        int count = ks.fireAllRules();
        System.out.println("总执行了"+count+"条规则");
        ks.dispose();
    }
}
