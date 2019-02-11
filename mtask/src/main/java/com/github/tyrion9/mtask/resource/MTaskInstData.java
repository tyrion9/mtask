package com.github.tyrion9.mtask.resource;

import com.github.tyrion9.mtask.MTaskInst;
import com.github.tyrion9.mtask.Scheduled;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class MTaskInstData {
    private String code;

    private String name;

    private String className;

    private boolean autoStart = false;

    private Scheduled scheduled;

    private Map<String, Object> dynamicParams = new HashMap<>();

    private MTaskInst.Status status;

    public static MTaskInstData of(MTaskInst mTaskInst) {
        return MTaskInstData.builder()
                .code(mTaskInst.getMTaskDefinition().getCode())
                .name(mTaskInst.getMTaskDefinition().getName())
                .className(mTaskInst.getMTaskDefinition().getClassName())
                .autoStart(mTaskInst.getMTaskDefinition().isAutoStart())
                .scheduled(mTaskInst.getMTaskDefinition().getScheduled())
                .dynamicParams(mTaskInst.getMTaskDefinition().getParams())
                .status(mTaskInst.getStatus())
                .build();
    }
}
