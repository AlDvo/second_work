package com.dvorenenko.action;

import com.dvorenenko.config.EntityCharacteristicConfig;
import com.dvorenenko.config.FieldSizeConfig;
import com.dvorenenko.config.PossibilityOfEatingConfig;
import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.animal.abstracts.Animal;
import com.dvorenenko.entity.animal.abstracts.Herbivore;
import com.dvorenenko.entity.animal.abstracts.Predator;
import com.dvorenenko.entity.animal.herbivore.Boar;
import com.dvorenenko.entity.animal.herbivore.Caterpillar;
import com.dvorenenko.entity.animal.herbivore.Duck;
import com.dvorenenko.entity.animal.herbivore.Mouse;
import com.dvorenenko.entity.enums.EntityType;
import com.dvorenenko.entity.grass.Grass;
import com.dvorenenko.location.Location;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class EatingService {

    public static final int LOWER_LIMIT_SATURATION = 4;

    public Location eat(Location location, Random random, EntityCharacteristicConfig characteristicConfig, PossibilityOfEatingConfig possibilityOfEatingConfig) {

        for (Map.Entry<FieldSizeConfig, List<Entity>> fieldSizeConfigListEntry : location.getIsland().entrySet()) {
            for (Entity entity : fieldSizeConfigListEntry.getValue()) {
                long probabilityWeTake = random.nextLong(100);
                if ( entity instanceof Herbivore){
                    herbivoreEat(characteristicConfig, fieldSizeConfigListEntry.getValue(), entity, probabilityWeTake, possibilityOfEatingConfig);
                }else if(entity instanceof Predator){
                    predatorEatHerbivore(characteristicConfig, fieldSizeConfigListEntry.getValue(), entity, probabilityWeTake, possibilityOfEatingConfig);
                }
            }
        }
        return location;
    }

    private void herbivoreEat(EntityCharacteristicConfig characteristicConfig, List<Entity> fieldSizeConfigListEntry, Entity entity, long probabilityWeTake, PossibilityOfEatingConfig possibilityOfEatingConfig) {
        if (entity instanceof Boar) {
            herbivoreEatMouse(characteristicConfig, fieldSizeConfigListEntry, entity, probabilityWeTake, possibilityOfEatingConfig);
        } else if (entity instanceof Mouse || entity instanceof Duck) {
            herbivoreEatCaterpillar(characteristicConfig, fieldSizeConfigListEntry, entity, probabilityWeTake, possibilityOfEatingConfig);
        } else {
            herbivoreEatGrass(characteristicConfig, fieldSizeConfigListEntry, entity);
        }
    }

    private void herbivoreEatGrass(EntityCharacteristicConfig characteristicConfig,
                                   List<Entity> fieldSizeConfigListEntry,
                                   Entity entity) {

        boolean herbivoreWhoEatGrass = entity instanceof Herbivore;
        boolean weHaveLiveGrass = fieldSizeConfigListEntry.stream().anyMatch(m -> m instanceof Grass && m.isAlive());

        if (herbivoreWhoEatGrass && weHaveLiveGrass) {
            setIsLiveFalseForEntity(fieldSizeConfigListEntry, Grass.class);
            //addEntityMealSaturation(characteristicConfig, entity, EntityType.GRASS);
        } else {
            decreaseSaturation(entity);
            checkLowerLimitSaturation(entity);
        }
    }

    private void herbivoreEatCaterpillar(EntityCharacteristicConfig characteristicConfig,
                                         List<Entity> fieldSizeConfigListEntry,
                                         Entity entity,
                                         long probabilityWeTake,
                                         PossibilityOfEatingConfig possibilityOfEatingConfig) {

        boolean weHaveLiveCaterpillar = fieldSizeConfigListEntry.stream().anyMatch(m -> m instanceof Caterpillar && m.isAlive());
        Long probabilityEat = determineProbability(entity, possibilityOfEatingConfig, new Caterpillar());

        if (weHaveLiveCaterpillar && probabilityWeTake <= probabilityEat) {
            setIsLiveFalseForEntity(fieldSizeConfigListEntry, Caterpillar.class);
            //addEntityMealSaturation(characteristicConfig, entity, EntityType.CATERPILLAR);
        } else {
            herbivoreEatGrass(characteristicConfig, fieldSizeConfigListEntry, entity);
        }
    }

    private void herbivoreEatMouse(EntityCharacteristicConfig characteristicConfig,
                                   List<Entity> fieldSizeConfigListEntry,
                                   Entity entity,
                                   long probabilityWeTake,
                                   PossibilityOfEatingConfig possibilityOfEatingConfig) {

        boolean weHaveLiveMouse = fieldSizeConfigListEntry.stream().anyMatch(m -> m instanceof Mouse && m.isAlive());
        Long probabilityEat = determineProbability(entity, possibilityOfEatingConfig, new Mouse());

        if (weHaveLiveMouse && probabilityWeTake <= probabilityEat) {
            setIsLiveFalseForEntity(fieldSizeConfigListEntry, Mouse.class);
            //addEntityMealSaturation(characteristicConfig, entity, EntityType.MOUSE);
        } else {
            herbivoreEatCaterpillar(characteristicConfig, fieldSizeConfigListEntry, entity, probabilityWeTake, possibilityOfEatingConfig);
        }
    }

    private void predatorEatHerbivore(EntityCharacteristicConfig characteristicConfig,
                                      List<Entity> fieldSizeConfigListEntry,
                                      Entity entity,
                                      long probabilityWeTake,
                                      PossibilityOfEatingConfig possibilityOfEatingConfig)  {
        Animal animal = getAnimalWhomPredatorCanEat(fieldSizeConfigListEntry, entity, probabilityWeTake, possibilityOfEatingConfig);
        if(animal != null){
            setIsLiveFalseForEntity(fieldSizeConfigListEntry , animal.getClass());
            //String nameEntityTypeHerbivore = animal.getClass().getSimpleName().toUpperCase();
            //addEntityMealSaturation(characteristicConfig, entity, EntityType.valueOf(nameEntityTypeHerbivore));
        }else {
            decreaseSaturation(entity);
            checkLowerLimitSaturation(entity);
        }
    }

    private Animal getAnimalWhomPredatorCanEat(List<Entity> fieldSizeConfigListEntry,
                                               Entity entity,
                                               long probabilityWeTake,
                                               PossibilityOfEatingConfig possibilityOfEatingConfig) {
        Animal animal = null;
        Map<Entity, Entity> hunterHunted = new HashMap<>();
        long maxValuePossibility = 0;

        Entity entityHunterForJsonConfig = getEntityForPossibilityOfEatingConfigToMap(entity);

        for (EntityType entityType : EntityType.values()) {
            Entity entityHuntedForJsonConfig = getEntityForPossibilityOfEatingConfigToMap(entityType);
            hunterHunted.put(entityHunterForJsonConfig, entityHuntedForJsonConfig);

            Long possibilityOfEating = getPossibilityOfEating(possibilityOfEatingConfig, entityHunterForJsonConfig, entityHuntedForJsonConfig, hunterHunted);

            boolean checkHaveAliveHerbivore = fieldSizeConfigListEntry.stream().filter(entityType.getClazz()::isInstance).anyMatch(Entity::isAlive);
            boolean checkPossibility = probabilityWeTake <= possibilityOfEating && maxValuePossibility > possibilityOfEating;

            if(checkPossibility && checkHaveAliveHerbivore && !entityHunterForJsonConfig.equals(entityHuntedForJsonConfig)){
                maxValuePossibility = possibilityOfEating;
                animal = (Animal) entityHuntedForJsonConfig;
            }
        }
        return animal;
    }

    private Long getPossibilityOfEating(PossibilityOfEatingConfig possibilityOfEatingConfig, Entity entityHunterForJsonConfig, Entity entityHuntedForJsonConfig, Map<Entity, Entity> hunterHunted) {
        Long possibilityOfEating = 0L;
        if(!entityHunterForJsonConfig.equals(entityHuntedForJsonConfig)){
            possibilityOfEating = possibilityOfEatingConfig.getPossibilityOfEatingConfigToMap().get(hunterHunted);
        }
        return possibilityOfEating;
    }


/*    private void addEntityMealSaturation(EntityCharacteristicConfig characteristicConfig, Entity entity, EntityType entityType) {
        String nameEntityTypeOfEntity = entity.getClass().getSimpleName().toUpperCase();
        if (entity.getMealKg() < characteristicConfig.getCharacteristicMapConfig().get(EntityType.valueOf(nameEntityTypeOfEntity)).getMealKg())
            entity.setMealKg(entity.getMealKg() + characteristicConfig.getCharacteristicMapConfig().get(entityType).getWeight());
    }*/

    private void setIsLiveFalseForEntity(List<Entity> fieldSizeConfigListEntry, Class<? extends Entity> type) {
        Entity entity = fieldSizeConfigListEntry.stream().filter(type::isInstance).filter(Entity::isAlive).findFirst().get();
        entity.setAlive(false);
    }

    private Long determineProbability(Entity entityHunter, PossibilityOfEatingConfig possibilityOfEatingConfig, Entity entityHunted) {
        Map<Entity, Entity> hunterHunted = new HashMap<>();
        Entity entityHunterForJsonConfig = getEntityForPossibilityOfEatingConfigToMap(entityHunter);
        hunterHunted.put(entityHunterForJsonConfig, entityHunted);

        return possibilityOfEatingConfig.getPossibilityOfEatingConfigToMap().get(hunterHunted);
    }

    private Entity getEntityForPossibilityOfEatingConfigToMap(Entity entityHunter) {
        Entity entityHunterForJsonConfig = null;
        try {
            Constructor<? extends Entity> constructorHunter = entityHunter.getClass().getConstructor();
            entityHunterForJsonConfig = constructorHunter.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return entityHunterForJsonConfig;
    }

    private Entity getEntityForPossibilityOfEatingConfigToMap(EntityType entityType) {
        Entity entityHuntedForJsonConfig = null;
        try {
            entityHuntedForJsonConfig = (Entity) entityType.getClazz().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return entityHuntedForJsonConfig;
    }
    private void checkLowerLimitSaturation(Entity entity) {
        if (entity.getCountOfHunger() >= LOWER_LIMIT_SATURATION) {
            entity.setAlive(false);
        }
    }

    private void decreaseSaturation(Entity entity) {
        entity.setCountOfHunger(entity.getCountOfHunger() + 1);
    }
}
