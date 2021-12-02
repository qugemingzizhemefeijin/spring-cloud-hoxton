package com.atguigu.springcloud.configuration;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DroolsConfiguration {

    @Bean
    public RuleTimeImpl ruleTimeImpl() {
        return new RuleTimeImpl();
    }

    @Bean
    public KieHelper kieHelper(){
        KieHelper kieHelper = new KieHelper();
        kieHelper.addFromClassPath("/rulesTwo/isString/isRuleRunTime.drl");
        return kieHelper;
    }

    @Bean("kbase")
    public KieBase kieBase(KieHelper kieHelper) {
        return kieHelper.build();
    }

    @Bean("ksession")
    public KieSession kieSession(KieBase kbase) {
        KieSession ksession = kbase.newKieSession();
        ksession.addEventListener(ruleTimeImpl());

        log.info("===================" + ksession);
        return ksession;
    }

}
