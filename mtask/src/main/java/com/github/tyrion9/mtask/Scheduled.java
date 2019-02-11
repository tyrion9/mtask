package com.github.tyrion9.mtask;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.scheduling.TaskScheduler;

import java.util.concurrent.ScheduledFuture;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.MINIMAL_CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "className")
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = FixedRate.class, name = "FixedRate"),
//        @JsonSubTypes.Type(value = Cron.class, name = "Cron")
//})
public interface Scheduled {
    ScheduledFuture schedule(Runnable runnable, TaskScheduler scheduler);
}
