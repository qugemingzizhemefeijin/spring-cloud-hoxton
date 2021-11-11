package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.service.KieService;
import com.atguigu.springcloud.vo.ReqAndResult;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class KieServiceImpl implements KieService {

    public static final String RULES_PATH = "rules/";
    public static final String BASE_RULES_PATH = "classpath*:";

    private StatelessKieSession kieSession;

    private KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();

    /*public KieFileSystem kieFileSystem() throws IOException {
        KieFileSystem kieFileSystem = getKieServices().newKieFileSystem();
        for (Resource file : getRuleFiles()) {
            kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + file.getFilename(), "UTF-8"));
        }
        return kieFileSystem;
    }*/

    private Resource[] getRuleFiles() throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        return resourcePatternResolver.getResources(BASE_RULES_PATH + RULES_PATH + "**/*.*");
    }

    @Override
    public void loadRules(String ruleCode) {
        loadRule();
    }

    @Override
    public void execute(Object object) {
        try{
            kieSession.execute(object);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public List<Object> executeReturnResult(ReqAndResult object) {
        this.kieSession.execute(object);
        return object.getResult();
    }

    /**
     * 单一规则上线
     */
    private void loadRule() {
        try {
            KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
            Resource[] resources = getRuleFiles();
            for(Resource resource : resources) {
                kbuilder.add(ResourceFactory.newClassPathResource(RULES_PATH + resource.getFilename(), "UTF-8"), ResourceType.DRL);
            }
            //kbuilder.add(ResourceFactory.newByteArrayResource(ruleContent.getBytes()), ResourceType.DRL);
            if (kbuilder.hasErrors()) {
                log.error("加载规则模板引擎异常{}", kbuilder.getErrors());
                return;
            }
            kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
            kieSession = kbase.newStatelessKieSession();
            printRules();
            setGlobal();
        } catch (Exception e) {
            log.error("加载规则模板引擎异常", e);
        }
    }

    private void setGlobal(){
        //kieSession.setGlobal("productRiskService", productRiskService);
        //kieSession.setGlobal("orderRiskService", orderRiskService);
    }

    private void printRules() {
        kbase.getKnowledgePackages().forEach(knowledgePackage ->
                knowledgePackage.getRules().forEach(rule ->
                        log.info("print rule: " + knowledgePackage.getName() + "." + rule.getName())));
    }

}
