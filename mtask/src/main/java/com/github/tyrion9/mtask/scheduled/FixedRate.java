package com.github.tyrion9.mtask.scheduled;

import com.github.tyrion9.mtask.Scheduled;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.TaskScheduler;

import java.util.concurrent.ScheduledFuture;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FixedRate implements Scheduled {
    private long period;  // in miliseconds

    @Override
    public ScheduledFuture schedule(Runnable runnable, TaskScheduler scheduler) {
        return scheduler.scheduleAtFixedRate(runnable, period);
    }
}
