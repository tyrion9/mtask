package com.github.tyrion9.mtask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

@Component("mtaskManager")
@DependsOn("mtaskScheduler")
public class MTaskManager {
    private Map<String, MTaskInst> mtasks = new LinkedHashMap();

    @Autowired
    private MTaskDefRepository repo;

    @Qualifier("mtaskScheduler")
    @Autowired
    private TaskScheduler scheduler;

    @Autowired
    DefaultListableBeanFactory factory;

    public void init() {
        List<MTaskDefinition> mTaskInstList = repo.loadAll();

        for (MTaskDefinition mTaskDefinition : mTaskInstList) {
            factory.registerBeanDefinition(mTaskDefinition.getBeanName(),
                    genericBeanDefinition(mTaskDefinition.getClassName())
                            .setScope("prototype")
                            .getBeanDefinition());

            MTaskInst mTaskInst = new MTaskInst();
            mTaskInst.setMTaskDefinition(mTaskDefinition);
            mTaskInst.init();

            mtasks.put(mTaskDefinition.getCode(), mTaskInst);
        }
    }

    public List<MTaskInst> listMTastInst(){
        return mtasks.values().stream().collect(Collectors.toList());
    }

    public void stop(String code) {
        mtasks.get(code).stop(false);
    }

    public void stopForce(String code) {
        mtasks.get(code).stop(true);
    }

    public void start(String code) {
        MTaskInst inst = mtasks.get(code);

        if (inst != null && inst.getStatus() == MTaskInst.Status.STOPPED)
            mtasks.get(code).start();
    }

    public boolean existMTaskCode(String code) {
        return mtasks.containsKey(code);
    }

    public void update(MTaskDefinition mtaskDef) {
        if (!mtasks.containsKey(mtaskDef.getCode()))
            throw new IllegalArgumentException("Not found MTaskDef " + mtaskDef.getCode());

        mtasks.get(mtaskDef.getCode()).setMTaskDefinition(mtaskDef);

        updateAllMTaskDefinition();
    }

    private void updateAllMTaskDefinition(){
        List<MTaskDefinition> lstMTaskDef = new ArrayList<>();

        for(MTaskInst inst : mtasks.values()) {
            lstMTaskDef.add(inst.getMTaskDefinition());
        }

        repo.saveAll(lstMTaskDef);
    }
}
