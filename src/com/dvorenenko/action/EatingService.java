package com.dvorenenko.action;

import com.dvorenenko.config.FieldSizeConfig;
import com.dvorenenko.config.PossibilityOfEatingConfig;
import com.dvorenenko.constants.Constants;
import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.animal.abstracts.Animal;
import com.dvorenenko.entity.enums.EntityType;
import com.dvorenenko.itteration.ChoseVariable;
import com.dvorenenko.location.Location;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class EatingService {

    public Location eat(Location location, Random random, PossibilityOfEatingConfig possibilityOfEatingConfig) {

        for (Map.Entry<FieldSizeConfig, List<Entity>> fieldSizeConfigListEntry : location.getIsland().entrySet()) {
            for (Entity entity : fieldSizeConfigListEntry.getValue()) {
                long probabilityWeTake = random.nextLong(100);
                if (entity instanceof Animal) {
                    predatorEatHerbivore(fieldSizeConfigListEntry.getValue(), entity, probabilityWeTake, possibilityOfEatingConfig);
                }
            }
        }
        return location;
    }

    private void predatorEatHerbivore(List<Entity> fieldSizeConfigListEntry,
                                      Entity entity,
                                      long probabilityWeTake,
                                      PossibilityOfEatingConfig possibilityOfEatingConfig) {

        Entity entityHunted = getAnimalWhomPredatorCanEat(fieldSizeConfigListEntry, entity, probabilityWeTake, possibilityOfEatingConfig);
        if (entityHunted != null) {
            setIsLiveFalseForEntity(fieldSizeConfigListEntry, entityHunted.getClass());
        } else {
            decreaseSaturation(entity);
            checkLowerLimitSaturation(entity);
        }
    }

    private Entity getAnimalWhomPredatorCanEat(List<Entity> fieldSizeConfigListEntry,
                                               Entity entity,
                                               long probabilityWeTake,
                                               PossibilityOfEatingConfig possibilityOfEatingConfig) {

        Entity entityHunted = null;
        Map<Entity, Entity> hunterHunted = new HashMap<>();
        long maxValuePossibility = 100;

        Entity entityHunterForJsonConfig = getEntityForPossibilityOfEatingConfigToMap(entity);

        for (EntityType entityType : EntityType.values()) {
            Entity entityHuntedForJsonConfig = getEntityForPossibilityOfEatingConfigToMap(entityType);
            hunterHunted.put(entityHunterForJsonConfig, entityHuntedForJsonConfig);

            Long possibilityOfEating = getPossibilityOfEating(possibilityOfEatingConfig, entityHunterForJsonConfig, entityHuntedForJsonConfig, hunterHunted);

            boolean checkHaveAliveHerbivore = fieldSizeConfigListEntry.stream().filter(entityType.getClazz()::isInstance).anyMatch(Entity::isAlive);
            boolean checkPossibility = probabilityWeTake <= possibilityOfEating && maxValuePossibility >= possibilityOfEating;

            if (checkPossibility && checkHaveAliveHerbivore && !entityHunterForJsonConfig.equals(entityHuntedForJsonConfig)) {
                maxValuePossibility = possibilityOfEating;
                entityHunted = entityHuntedForJsonConfig;
            }
        }
        return entityHunted;
    }

    private Long getPossibilityOfEating(PossibilityOfEatingConfig possibilityOfEatingConfig,
                                        Entity entityHunterForJsonConfig,
                                        Entity entityHuntedForJsonConfig,
                                        Map<Entity, Entity> hunterHunted) {
        Long possibilityOfEating = 0L;
        if (!entityHunterForJsonConfig.equals(entityHuntedForJsonConfig)) {
            possibilityOfEating = possibilityOfEatingConfig.getPossibilityOfEatingConfigToMap().get(hunterHunted);
        }
        return possibilityOfEating;
    }

    private void setIsLiveFalseForEntity(List<Entity> fieldSizeConfigListEntry, Class<? extends Entity> type) {
        Entity entity = fieldSizeConfigListEntry.stream().filter(type::isInstance).filter(Entity::isAlive).findFirst().get();
        entity.setAlive(false);
    }

    private Entity getEntityForPossibilityOfEatingConfigToMap(Entity entityHunter) {
        Entity entityHunterForJsonConfig;
        try {
            Constructor<? extends Entity> constructorHunter = entityHunter.getClass().getConstructor();
            entityHunterForJsonConfig = constructorHunter.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return entityHunterForJsonConfig;
    }

    private Entity getEntityForPossibilityOfEatingConfigToMap(EntityType entityType) {
        Entity entityHuntedForJsonConfig;
        try {
            entityHuntedForJsonConfig = (Entity) entityType.getClazz().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return entityHuntedForJsonConfig;
    }

    private void checkLowerLimitSaturation(Entity entity) {
        if (entity.getCountOfHunger() > ChoseVariable.LOWER_LIMIT_SATURATION) {
            entity.setAlive(false);
        }
    }

    private void decreaseSaturation(Entity entity) {
        entity.setCountOfHunger(entity.getCountOfHunger() + Constants.ADD_DAY_HUNGER);
    }
}
