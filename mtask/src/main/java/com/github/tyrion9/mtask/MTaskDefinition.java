package com.github.tyrion9.mtask;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MTaskDefinition {
    private String code;

    private String name;

    private String className;

    @Builder.Default
    private boolean autoStart = false;

    private Scheduled scheduled;

    private Map<String, Object> params = new HashMap<>();

    public String getBeanName(){
        return "mtask$" + code;
    }
}
