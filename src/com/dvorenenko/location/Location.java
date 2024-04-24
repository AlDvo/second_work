package com.dvorenenko.location;

import com.dvorenenko.config.EntityCharacteristicConfig;
import com.dvorenenko.config.FieldSizeConfig;
import com.dvorenenko.config.MakeClassByReflection;
import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.enums.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Location {

    private Map<FieldSizeConfig, List<Entity>> island;

    public Location(Random random, EntityCharacteristicConfig characteristicConfig, FieldSizeConfig fieldSizeConfig) {
        this.island = fillLocationByEntity(random, characteristicConfig, fieldSizeConfig);
    }

    public Location(Map<FieldSizeConfig, List<Entity>> island) {
        this.island = island;
    }

    public Map<FieldSizeConfig, List<Entity>> getIsland() {
        return island;
    }

    private Map<FieldSizeConfig, List<Entity>> fillLocationByEntity(Random random, EntityCharacteristicConfig characteristicConfig, FieldSizeConfig fieldSizeConfig) {
        Map<EntityType, Entity> characteristicMapConfig = characteristicConfig.getCharacteristicMapConfig();
        Map<FieldSizeConfig, List<Entity>> island = makeLocationWithoutAnimal(fieldSizeConfig);
        MakeClassByReflection makeClassByReflection = new MakeClassByReflection();

        for (Map.Entry<EntityType, Entity> entityTypeEntityEntry : characteristicMapConfig.entrySet()) {
            addEveryAnimalOnLocationCell(random, entityTypeEntityEntry.getValue(), island, makeClassByReflection);
        }
        return island;
    }

    private void addEveryAnimalOnLocationCell(Random random, Entity entityTypeEntityEntry, Map<FieldSizeConfig, List<Entity>> island, MakeClassByReflection makeClassByReflection) {
        for (Map.Entry<FieldSizeConfig, List<Entity>> listEntry : island.entrySet()) {
            int maxCount = random.nextInt(entityTypeEntityEntry.getMaxQtyOnCell());
            for (int i = 0; i < maxCount; i++) {
                Entity entity = makeClassByReflection.MakeClassByEntity(entityTypeEntityEntry);
                listEntry.getValue().add(entity);
            }
        }
    }

    private Map<FieldSizeConfig, List<Entity>> makeLocationWithoutAnimal(FieldSizeConfig fieldSizeConfig) {
        Map<FieldSizeConfig, List<Entity>> island = new HashMap<>();

        for (int i = 0; i < fieldSizeConfig.getHeight(); i++) {
            for (int j = 0; j < fieldSizeConfig.getWeight(); j++) {
                island.put(new FieldSizeConfig(i, j), new ArrayList<>());
            }
        }
        return island;
    }

}
