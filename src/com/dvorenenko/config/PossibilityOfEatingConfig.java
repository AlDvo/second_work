package com.dvorenenko.config;

import com.dvorenenko.action.PossibilityOfEating;
import com.dvorenenko.constants.Constants;
import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.enums.EntityType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PossibilityOfEatingConfig {

    private Map<Map<Entity, Entity>, Long> possibilityOfEatingConfig;

    public PossibilityOfEatingConfig(ObjectMapper objectMapper, String pathToJson){
        File file = new File(pathToJson);
        this.possibilityOfEatingConfig = getPossibilityOfEatingConfigToMap(objectMapper, file);
    }

    public Map<Map<Entity, Entity>, Long> getPossibilityOfEatingConfigToMap() {
        return possibilityOfEatingConfig;
    }

    private Map<Map<Entity, Entity>, Long> getPossibilityOfEatingConfigToMap(ObjectMapper objectMapper, File file) {

        PossibilityOfEating[] possibilityOfEating = extractedPossibility(objectMapper, file);

        Map<Map<Entity, Entity>, Long> possibilityOfEatingConfig = new HashMap<>();
        Entity entityHunter = null;
        Entity entityHunted = null;
        for (PossibilityOfEating possibility : possibilityOfEating) {
            for (EntityType value : EntityType.values()) {
                if (possibility.getFrom().equals(value.getType())) {
                    entityHunter = getEntity(value);
                } else if (possibility.getTo().equals(value.getType())) {
                    entityHunted = getEntity(value);
                }
            }
            Map<Entity, Entity> entity = new HashMap<>();
            entity.put(entityHunter, entityHunted);
            possibilityOfEatingConfig.put(entity, possibility.getPercent());
        }
        return possibilityOfEatingConfig;
    }

    private Entity getEntity(EntityType value) {
        Entity entity;
        Constructor<?> entityConstructor;
        try {
            entityConstructor = value.getClazz().getConstructor();
            entity = (Entity) entityConstructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            System.out.println(Constants.ERROR_MAKE_ENTITY);
            throw new RuntimeException(e);
        }
        return entity;
    }

    private PossibilityOfEating[] extractedPossibility(ObjectMapper objectMapper, File file) {
        PossibilityOfEating[] possibility;
        try {
            possibility = objectMapper.readValue(file, PossibilityOfEating[].class);
        } catch (IOException e) {
            System.out.println(Constants.ERROR_PARSING);
            throw new RuntimeException(e);
        }
        return possibility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PossibilityOfEatingConfig that = (PossibilityOfEatingConfig) o;
        return Objects.equals(possibilityOfEatingConfig, that.possibilityOfEatingConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(possibilityOfEatingConfig);
    }
}
