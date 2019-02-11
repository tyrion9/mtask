package com.github.tyrion9.mtask;

import lombok.Data;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Data
public class MTaskInst {
    private static final Logger log = LoggerFactory.getLogger(MTaskInst.class);

    public enum Status {STOPPED, RUNNING}

    private Status status = Status.STOPPED;

    private MTaskDefinition mTaskDefinition;

    private MTask mtask;

    private ScheduledFuture future;

    public void init(){
        initMTask();

        if (mTaskDefinition.isAutoStart())
            start();
    }

    private void initMTask(){
        try {
            mtask = (MTask) SpringContextUtil.getApplicationContext().getBean(mTaskDefinition.getBeanName());

            mtask.mlog.setMtaskCode(mTaskDefinition.getCode());

            List<Field> fields = FieldUtils.getFieldsListWithAnnotation(mtask.getClass(), MTaskParam.class);

            for(Field field : fields) {
                for(Map.Entry<String, Object> entry : mTaskDefinition.getParams().entrySet()){
                    MTaskParam p = field.getAnnotation(MTaskParam.class);
                    if (p.value().equals(entry.getKey())) {
                        if (!field.isAccessible())
                            field.setAccessible(true);

                        FieldUtils.writeField(field, mtask, entry.getValue());
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't Initialize MTask from class " + mTaskDefinition.getClassName(), e);
        }
    }

    public void stop(boolean force){
        log.debug("Stopping {} with force = {}", mTaskDefinition.getCode(), force);

        if (!force)
            mtask.interrupted = true;

        future.cancel(force);
        mtask.mlog.log("Stop");

        mtask = null;

        status = Status.STOPPED;
        future = null;

        log.debug("Stopped {} with force = {}", mTaskDefinition.getCode(), force);
    }

    public void start() {
        log.debug("Starting MTask: {}", mTaskDefinition);

        initMTask();

        future = mTaskDefinition.getScheduled().schedule(mtask, SpringContextUtil.getTaskScheduler());
        status = Status.RUNNING;

        mtask.mlog.log("Start");
        log.debug("Started MTask: {}", mTaskDefinition);
    }
}
