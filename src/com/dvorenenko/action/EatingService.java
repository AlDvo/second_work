package com.dvorenenko.action;

import com.dvorenenko.config.EntityCharacteristicConfig;
import com.dvorenenko.config.FieldSizeConfig;
import com.dvorenenko.config.MakeClassByReflection;
import com.dvorenenko.config.PossibilityOfEatingConfig;
import com.dvorenenko.constants.Constants;
import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.animal.abstracts.Animal;
import com.dvorenenko.entity.enums.EntityType;
import com.dvorenenko.location.Location;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static com.dvorenenko.itteration.ChangeVariableService.LOWER_LIMIT_SATURATION;


public class EatingService {

    public Location eat(Location location, 
                        Random random, 
                        PossibilityOfEatingConfig possibilityOfEatingConfig, 
                        MakeClassByReflection makeClassByReflection,
                        EntityCharacteristicConfig entityCharacteristicConfig) {
        for (Map.Entry<FieldSizeConfig, List<Entity>> fieldSizeConfigListEntry : location.getIsland().entrySet()) {
            for (Entity entity : fieldSizeConfigListEntry.getValue()) {
                long probabilityWeTake = random.nextLong(Constants.MAX_PERCENT);
                if (entity instanceof Animal) {
                    entityEatingAnotherEntity(fieldSizeConfigListEntry.getValue(), 
                            entity, 
                            probabilityWeTake, 
                            possibilityOfEatingConfig, 
                            makeClassByReflection, 
                            entityCharacteristicConfig);
                }
            }
        }
        return location;
    }

    private void entityEatingAnotherEntity(List<Entity> fieldSizeConfigListEntry,
                                           Entity entity,
                                           long probabilityWeTake,
                                           PossibilityOfEatingConfig possibilityOfEatingConfig,
                                           MakeClassByReflection makeClassByReflection,
                                           EntityCharacteristicConfig entityCharacteristicConfig) {

        Entity entityHunted = getAnimalWhomPredatorCanEat(fieldSizeConfigListEntry,
                entity,
                probabilityWeTake,
                possibilityOfEatingConfig,
                makeClassByReflection);

        if (entityHunted != null && checkSaturationOfAnimal(entityCharacteristicConfig, entity, entityHunted)) {
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
        long maxValuePossibility = Constants.MAX_PERCENT;

        Entity entityHunterForJsonConfig = makeClassByReflection.makeClassByEntity(entity);

        for (EntityType entityType : EntityType.values()) {
            Entity entityHuntedForJsonConfig = makeClassByReflection.makeClassByEntityType(entityType);
            hunterHunted.put(entityHunterForJsonConfig, entityHuntedForJsonConfig);

            Long possibilityOfEating = getPossibilityOfEating(possibilityOfEatingConfig, entityHunterForJsonConfig, entityHuntedForJsonConfig, hunterHunted);

            if (isCheckAliveEntityCheckPossibility(fieldSizeConfigListEntry,
                    probabilityWeTake,
                    entityType,
                    possibilityOfEating,
                    maxValuePossibility,
                    entityHunterForJsonConfig,
                    entityHuntedForJsonConfig)) {

                maxValuePossibility = possibilityOfEating;
                entityHunted = entityHuntedForJsonConfig;
            }
        }
        return entityHunted;
    }

    private boolean isCheckAliveEntityCheckPossibility(List<Entity> fieldSizeConfigListEntry,
                                                       Long probabilityWeTake,
                                                       EntityType entityType,
                                                       Long possibilityOfEating,
                                                       Long maxValuePossibility,
                                                       Entity entityHunterForJsonConfig,
                                                       Entity entityHuntedForJsonConfig) {

        return checkPossibilityFromJsonWithPossibilityWeTake(probabilityWeTake, possibilityOfEating, maxValuePossibility) &&
                isCheckHaveAliveHunted(fieldSizeConfigListEntry, entityType) &&
                !entityHunterForJsonConfig.equals(entityHuntedForJsonConfig);
    }

    private boolean isCheckHaveAliveHunted(List<Entity> fieldSizeConfigListEntry, EntityType entityType) {
        return fieldSizeConfigListEntry.stream()
                .filter(entityType.getClazz()::isInstance)
                .anyMatch(Entity::isAlive);
    }

    private boolean checkPossibilityFromJsonWithPossibilityWeTake(long probabilityWeTake, Long possibilityOfEating, long maxValuePossibility) {
        return probabilityWeTake <= possibilityOfEating && maxValuePossibility >= possibilityOfEating;
    }

    private Long getPossibilityOfEating(PossibilityOfEatingConfig possibilityOfEatingConfig,
                                        Entity entityHunterForJsonConfig,
                                        Entity entityHuntedForJsonConfig,
                                        Map<Entity, Entity> hunterHunted) {
        Long possibilityOfEating = Constants.MIN_POSSIBILITY_OF_EATING;
        if (!entityHunterForJsonConfig.equals(entityHuntedForJsonConfig)) {
            possibilityOfEating = possibilityOfEatingConfig.fillPossibilityOfEatingConfigToMap().get(hunterHunted);
        }
        return possibilityOfEating;
    }

    private void setIsLiveFalseForEntity(List<Entity> fieldSizeConfigListEntry, Class<? extends Entity> type) {
        Optional<Entity> entity = fieldSizeConfigListEntry.stream().filter(type::isInstance).filter(Entity::isAlive).findFirst();
        entity.ifPresent(value -> value.setAlive(false));
    }

    private void checkLowerLimitSaturation(Entity entity) {
        if (entity.getCountOfHunger() > LOWER_LIMIT_SATURATION) {
            entity.setAlive(false);
        }
    }

    private void decreaseSaturation(Entity entity) {
        entity.setCountOfHunger(entity.getCountOfHunger() + Constants.ADD_DAY_HUNGER);
    }
    
    private boolean checkSaturationOfAnimal(EntityCharacteristicConfig entityCharacteristicConfig, Entity entityHunter, Entity entityHunted){
        return getWeightEntity(entityCharacteristicConfig, entityHunted) >=  entityHunter.getMealKg() / LOWER_LIMIT_SATURATION;
    }

    private double getWeightEntity(EntityCharacteristicConfig entityCharacteristicConfig, Entity entity){
        EntityType entityType = getEntityTypeByEntity(entity);
        return entityCharacteristicConfig.getCharacteristicMapConfig().get(entityType).getWeight();
    }

    private EntityType getEntityTypeByEntity(Entity entity) {
        return Arrays.stream(EntityType.values()).
                filter(value -> value.getType().equals(entity.getClass().getSimpleName().toLowerCase())).
                findFirst().
                orElse(null);
    }

}
