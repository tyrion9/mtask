package com.github.tyrion9.mtask.scheduled;

import com.github.tyrion9.mtask.Scheduled;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ScheduledFuture;

public class Cron implements Scheduled {
    private String cron;

    public Cron(String cron) {
        this.cron = cron;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    @Override
    public ScheduledFuture schedule(Runnable runnable, TaskScheduler scheduler) {
        return scheduler.schedule(runnable, new CronTrigger(cron));
    }

    @Override
    public String toString(){
        return "Cron (" + cron + ")";
    }
}
