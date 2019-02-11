package com.github.tyrion9.mtask.resource;

import com.github.tyrion9.mtask.Scheduled;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MTaskDefinitionUpdate {
    private String name;

    private String className;

    private boolean autoStart;

    private Scheduled scheduled;

    private Map<String, Object> params = new HashMap<>();
}
