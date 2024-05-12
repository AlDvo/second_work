package com.dvorenenko.action;

import com.dvorenenko.config.EntityCharacteristicConfig;
import com.dvorenenko.config.FieldSizeConfig;
import com.dvorenenko.config.MakeClassByReflection;
import com.dvorenenko.constants.Constants;
import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.enums.EntityType;
import com.dvorenenko.location.Location;

import java.util.List;
import java.util.Map;

public class MultiplyService {

    public Location multiplynimalOnLocation(Location location, EntityCharacteristicConfig entityCharacteristicConfig, MakeClassByReflection makeClassByReflection) {

        for (var listEntry : location.getIsland().entrySet()) {
            for (EntityType entityType : EntityType.values()) {
                if (entityType != EntityType.GRASS) {
                    countCoupleOfAnimalAndAddChild(entityCharacteristicConfig, listEntry.getValue(), entityType, makeClassByReflection);
                } else {
                    multiplyGrass(entityCharacteristicConfig, listEntry, entityType, makeClassByReflection);
                }
            }
        }
        return location;
    }

    private void multiplyGrass(EntityCharacteristicConfig entityCharacteristicConfig, Map.Entry<FieldSizeConfig, List<Entity>> listEntry, EntityType entityType, MakeClassByReflection makeClassByReflection) {
        Class<?> type = entityType.getClazz();
        int countEntityType = (int) listEntry.getValue().stream().filter(type::isInstance).count();
        addNewEntityOnLocation(listEntry.getValue(), entityType, countEntityType, countEntityType, entityCharacteristicConfig, makeClassByReflection);
    }

    private void countCoupleOfAnimalAndAddChild(EntityCharacteristicConfig entityCharacteristicConfig,
                                                List<Entity> listEntry,
                                                EntityType entityType,
                                                MakeClassByReflection makeClassByReflection) {
        Class<?> type = entityType.getClazz();
        long countEntityType = listEntry.stream().filter(type::isInstance).count();
        int quantityCoupleOfAnimal = (int) (countEntityType / Constants.COUPLE_ANIMAL);
        addNewEntityOnLocation(listEntry, entityType, quantityCoupleOfAnimal, countEntityType, entityCharacteristicConfig, makeClassByReflection);
    }

    private void addNewEntityOnLocation(List<Entity> listEntry,
                                        EntityType entityType,
                                        int quantityCoupleOfAnimal,
                                        long countEntityType,
                                        EntityCharacteristicConfig entityCharacteristicConfig,
                                        MakeClassByReflection makeClassByReflection) {

        for (int i = 0; i < quantityCoupleOfAnimal; i++) {
            int maxQuantityOnCell = entityCharacteristicConfig.getCharacteristicMapConfig().get(entityType).getMaxQuantityOnCell();
            double weight = entityCharacteristicConfig.getCharacteristicMapConfig().get(entityType).getWeight();
            int speed = entityCharacteristicConfig.getCharacteristicMapConfig().get(entityType).getSpeed();
            double mealKg = entityCharacteristicConfig.getCharacteristicMapConfig().get(entityType).getMealKg();

            Entity newAnimal = makeClassByReflection.makeClassByEntityType(entityType, weight, speed, mealKg, maxQuantityOnCell);
            if (countEntityType < maxQuantityOnCell) {
                listEntry.add(newAnimal);
            }
        }
    }
}
