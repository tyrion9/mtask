package com.github.tyrion9.mtask.repository;

import com.github.tyrion9.mtask.MTaskDefRepository;
import com.github.tyrion9.mtask.MTaskDefinition;
import com.github.tyrion9.mtask.Scheduled;
import com.github.tyrion9.mtask.scheduled.Cron;
import com.github.tyrion9.mtask.scheduled.FixedRate;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class YamlMTaskDefRepository implements MTaskDefRepository {

    private static final Logger log = LoggerFactory.getLogger(YamlMTaskDefRepository.class);

    public static final String DEFAULT_FILE = "mtasks.yml";

    private Yaml yamlMTaskDef;

    public YamlMTaskDefRepository(){
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setIndent(4);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        yamlMTaskDef = new Yaml(options);

        log.debug("init YamlMTaskDefRepository with yaml {}", yamlMTaskDef);
    }

    @Override
    public synchronized void save(MTaskDefinition mTaskDefinition) {

    }

    @Override
    public synchronized MTaskDefinition load(String mtaskCode) {
        return null;
    }

    @Override
    public synchronized List<MTaskDefinition> loadAll() {
        List<MTaskDefinition> mtasks = new ArrayList<>();

        try (InputStream is = new FileInputStream(DEFAULT_FILE)){
            List<Map<String, Object>> lstMTaskDef = yamlMTaskDef.<List<Map<String, Object>>>load(is);

            for(Map<String, Object> mapMTaskDef : lstMTaskDef) {
                mtasks.add(readMap(mapMTaskDef));
            }

            return mtasks;
        } catch (IOException ex){
            ex.printStackTrace();
            throw new IllegalStateException("Problem with reading yaml config " + DEFAULT_FILE, ex);
        }
    }

    private MTaskDefinition readMap(Map<String, Object> mapMTaskDef) {
        MTaskDefinition def = new MTaskDefinition();
        return def.builder()
                .code((String)mapMTaskDef.get("code"))
                .name((String)mapMTaskDef.get("name"))
                .className((String)mapMTaskDef.get("className"))
                .autoStart((Boolean)mapMTaskDef.get("autoStart"))
                .params((Map)mapMTaskDef.get("params"))
                .scheduled(readScheduled((Map)mapMTaskDef.get("scheduled")))
                .build();
    }

    private Scheduled readScheduled(Map<String, Object> scheduledDef) {
        Validate.isTrue(scheduledDef != null && scheduledDef.size() > 0, "No define scheduled");

        if (scheduledDef.get("period") != null) {
            return new FixedRate((Integer) scheduledDef.get("period"));
        } else if (scheduledDef.get("cron") != null) {
            return new Cron((String) scheduledDef.get("cron"));
        }

        throw new IllegalStateException("Unknown scheduled");
    }

    private Map<String, Object> writeScheduled(Scheduled scheduled) {
        Map<String, Object> mapSchedule = new HashMap();

        if (scheduled instanceof FixedRate)
            mapSchedule.put("period", ((FixedRate) scheduled).getPeriod());
        else if (scheduled instanceof Cron)
            mapSchedule.put("cron", ((Cron) scheduled).getCron());
        else
            throw new IllegalStateException("Unknown scheduled");

        return mapSchedule;
    }

    @Override
    public synchronized void saveAll(List<MTaskDefinition> mTaskDefinitionList) {
        List<Map<String, Object>> root = new ArrayList<>();

        for(MTaskDefinition def : mTaskDefinitionList) {
            Map<String, Object> node = new HashMap<>();
            node.put("code", def.getCode());
            node.put("name", def.getName());
            node.put("className", def.getClassName());
            node.put("autoStart", def.isAutoStart());
            node.put("params", def.getParams());
            node.put("scheduled", writeScheduled(def.getScheduled()));

            root.add(node);
        }

        try {
            yamlMTaskDef.dump(root.iterator(), new FileWriter(DEFAULT_FILE, false));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Problem with reading yaml config " + DEFAULT_FILE);
        }
    }
}
