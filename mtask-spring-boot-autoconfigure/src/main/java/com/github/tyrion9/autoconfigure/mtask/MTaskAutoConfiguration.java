package com.github.tyrion9.autoconfigure.mtask;

import com.github.tyrion9.mtask.MSocketEndpoint;
import com.github.tyrion9.mtask.MTask;
import com.github.tyrion9.mtask.MTaskManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@ConditionalOnClass(MTask.class)
@EnableWebSocket
@EnableScheduling
@EnableConfigurationProperties(MTaskProperties.class)
@ComponentScan(basePackages = {"com.github.tyrion9.mtask"})
public class MTaskAutoConfiguration {
    @Bean
    public MSocketEndpoint mSocketEndpoint() {
        MSocketEndpoint msocket = new MSocketEndpoint();
        return msocket;
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean("mtaskScheduler")
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(3);

        return taskScheduler;
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx, MTaskManager mTaskManager) {
        return args -> {
            mTaskManager.init();
        };
    }
}
