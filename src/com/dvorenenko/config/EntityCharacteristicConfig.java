package com.dvorenenko.config;

import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.enums.EntityType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.dvorenenko.constants.Constants.ERROR_PARSING;

public class EntityCharacteristicConfig {
    private Map<EntityType, Entity> characteristicMapConfig = new HashMap<>();

    public EntityCharacteristicConfig(ObjectMapper objectMapper, String pathToJson, MakeClassByReflection makeClassByReflection) {
        File file = new File(pathToJson);
        fillEntityTypeEntityValueToMap(objectMapper, file, makeClassByReflection);
    }

    public Map<EntityType, Entity> getCharacteristicMapConfig() {
        return characteristicMapConfig;
    }

    private void fillEntityTypeEntityValueToMap(ObjectMapper objectMapper, File file, MakeClassByReflection makeClassByReflection) {
        Entity entity;

        for (EntityType value : EntityType.values()) {
            try {
                JsonNode jsonNode = objectMapper.readTree(file);
                JsonNode specificObject = jsonNode.get(value.getType());
                double weight = specificObject.get("weight").asDouble();
                int speed = specificObject.get("speed").asInt();
                double mealKg = specificObject.get("mealKg").asDouble();
                int maxQtyOnCell = specificObject.get("maxQtyOnCell").asInt();

                entity = makeClassByReflection.makeClassByEntityType(value, weight,speed,mealKg,maxQtyOnCell);
            } catch (IOException e) {
                System.err.println(ERROR_PARSING);
                throw new RuntimeException(e);
            }
            this.characteristicMapConfig.put(value, entity);
        }
    }
}
