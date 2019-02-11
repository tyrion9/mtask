package com.github.tyrion9.mtask.resource;

import com.github.tyrion9.mtask.MTaskDefinition;
import com.github.tyrion9.mtask.MTaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class MTaskResource {
    @Autowired
    MTaskManager mTaskManager;

    @RequestMapping("")
    public List<MTaskInstData> listAll(){
        return mTaskManager.listMTastInst()
                .stream()
                .map(T -> MTaskInstData.of(T))
                .collect(Collectors.toList());
    }

    @PostMapping("/{code}/stop")
    public void stop(@PathVariable String code){
        mTaskManager.stop(code);
    }

    @PostMapping("/{code}/start")
    public void start(@PathVariable String code){
        mTaskManager.start(code);
    }

    @PutMapping("/{code}")
    public void update(@PathVariable String code, @RequestBody MTaskDefinitionUpdate update) {
        MTaskDefinition defUpdate = MTaskDefinition.builder()
                .code(code)
                .name(update.getName())
                .className(update.getClassName())
                .scheduled(update.getScheduled())
                .params(update.getParams())
                .autoStart(update.isAutoStart())
                .build();

        mTaskManager.update(defUpdate);
    }
}
