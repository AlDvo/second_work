package com.dvorenenko.action;

import com.dvorenenko.config.FieldSizeConfig;
import com.dvorenenko.config.MakeClassByReflection;
import com.dvorenenko.config.PossibilityOfEatingConfig;
import com.dvorenenko.constants.Constants;
import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.animal.abstracts.Animal;
import com.dvorenenko.entity.enums.EntityType;
import com.dvorenenko.itteration.ChangeVariableService;
import com.dvorenenko.location.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class EatingService {

    public Location eat(Location location, Random random, PossibilityOfEatingConfig possibilityOfEatingConfig, MakeClassByReflection makeClassByReflection) {
        for (Map.Entry<FieldSizeConfig, List<Entity>> fieldSizeConfigListEntry : location.getIsland().entrySet()) {
            for (Entity entity : fieldSizeConfigListEntry.getValue()) {
                long probabilityWeTake = random.nextLong(100);
                if (entity instanceof Animal) {
                    predatorEatHerbivore(fieldSizeConfigListEntry.getValue(), entity, probabilityWeTake, possibilityOfEatingConfig, makeClassByReflection);
                }
            }
        }
        return location;
    }

    private void predatorEatHerbivore(List<Entity> fieldSizeConfigListEntry,
                                      Entity entity,
                                      long probabilityWeTake,
                                      PossibilityOfEatingConfig possibilityOfEatingConfig,
                                      MakeClassByReflection makeClassByReflection) {

        Entity entityHunted = getAnimalWhomPredatorCanEat(fieldSizeConfigListEntry, entity, probabilityWeTake, possibilityOfEatingConfig, makeClassByReflection);
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
                                               PossibilityOfEatingConfig possibilityOfEatingConfig,
                                               MakeClassByReflection makeClassByReflection) {

        Entity entityHunted = null;
        Map<Entity, Entity> hunterHunted = new HashMap<>();
        long maxValuePossibility = 100;

        Entity entityHunterForJsonConfig = makeClassByReflection.MakeClassByEntity(entity);

        for (EntityType entityType : EntityType.values()) {
            Entity entityHuntedForJsonConfig = makeClassByReflection.MakeClassByEntityType(entityType);
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

    private void checkLowerLimitSaturation(Entity entity) {
        if (entity.getCountOfHunger() > ChangeVariableService.LOWER_LIMIT_SATURATION) {
            entity.setAlive(false);
        }
    }

    private void decreaseSaturation(Entity entity) {
        entity.setCountOfHunger(entity.getCountOfHunger() + Constants.ADD_DAY_HUNGER);
    }
}
