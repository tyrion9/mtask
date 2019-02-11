package com.github.tyrion9.mtask;

import java.util.List;

public interface MTaskDefRepository {
    void save(MTaskDefinition mTaskDefinition);

    MTaskDefinition load(String mtaskCode);

    List<MTaskDefinition> loadAll();

    void saveAll(List<MTaskDefinition> mTaskDefinitionList);
}
