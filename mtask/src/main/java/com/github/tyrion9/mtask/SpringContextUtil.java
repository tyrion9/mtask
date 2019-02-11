package com.github.tyrion9.mtask;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;

@Configuration
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static TaskScheduler getTaskScheduler(){
        return (TaskScheduler) context.getBean("mtaskScheduler");
    }

    public static MTaskManager getMTaskManager(){
        return (MTaskManager) context.getBean("mtaskManager");
    }
}