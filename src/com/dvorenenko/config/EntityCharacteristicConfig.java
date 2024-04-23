package com.dvorenenko.config;

import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.enums.EntityType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class EntityCharacteristicConfig {
    private Map<EntityType, Entity> characteristicMapConfig;

    public EntityCharacteristicConfig(ObjectMapper objectMapper, String pathToJson) {
        File file = new File(pathToJson);
        this.characteristicMapConfig = getEntityTypeEntityValueToMap(objectMapper, file);

    }

    public Map<EntityType, Entity> getCharacteristicMapConfig() {
        return characteristicMapConfig;
    }

    private Map<EntityType, Entity> getEntityTypeEntityValueToMap(ObjectMapper objectMapper, File file) {
        Entity entity;
        Map<EntityType, Entity> characteristicMapConfig = new HashMap<>();

        for (EntityType value : EntityType.values()) {
            try {
                JsonNode jsonNode = objectMapper.readTree(file);
                JsonNode specificObject = jsonNode.get(value.getType());
                double weight = specificObject.get("weight").asDouble();
                int speed = specificObject.get("speed").asInt();
                double mealKg = specificObject.get("mealKg").asDouble();
                int maxQtyOnCell = specificObject.get("maxQtyOnCell").asInt();

                Constructor<?> constructor = value.getClazz().getConstructor(double.class, int.class, double.class, int.class);
                entity = (Entity) constructor.newInstance(weight, speed, mealKg, maxQtyOnCell);
            } catch (IOException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            characteristicMapConfig.put(value, entity);
        }
        return characteristicMapConfig;
    }
}
