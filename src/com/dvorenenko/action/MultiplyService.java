package com.dvorenenko.action;

import com.dvorenenko.config.EntityCharacteristicConfig;
import com.dvorenenko.config.FieldSizeConfig;
import com.dvorenenko.config.MakeClassByReflection;
import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.enums.EntityType;
import com.dvorenenko.location.Location;

import java.util.List;
import java.util.Map;

public class MultiplyService {

    public Location multiplynimalOnLocation(Location location, EntityCharacteristicConfig entityCharacteristicConfig, MakeClassByReflection makeClassByReflection) {

        for (Map.Entry<FieldSizeConfig, List<Entity>> listEntry : location.getIsland().entrySet()) {
            for (EntityType entityType : EntityType.values()) {
                if (entityType != EntityType.GRASS) {
                    countCoupleOfAnimalAndAddChild(entityCharacteristicConfig, listEntry.getValue(), entityType, makeClassByReflection);
                }else{
                    multiplyGrass(entityCharacteristicConfig, listEntry, entityType, makeClassByReflection);
                }
            }
        }
        return location;
    }

    private void multiplyGrass(EntityCharacteristicConfig entityCharacteristicConfig, Map.Entry<FieldSizeConfig, List<Entity>> listEntry, EntityType entityType, MakeClassByReflection makeClassByReflection) {
        Class type = entityType.getClazz();
        int countEntityType = (int) listEntry.getValue().stream().filter(type::isInstance).count();
        addNewEntityOnLocation(listEntry.getValue(), entityType,countEntityType,countEntityType, entityCharacteristicConfig, makeClassByReflection);
    }

    private void countCoupleOfAnimalAndAddChild(EntityCharacteristicConfig entityCharacteristicConfig,
                                                List<Entity> listEntry,
                                                EntityType entityType,
                                                MakeClassByReflection makeClassByReflection) {
        Class type = entityType.getClazz();
        long countEntityType = listEntry.stream().filter(type::isInstance).count();
        int qtyCoupleOfAnimal = (int) (countEntityType / 2);
        addNewEntityOnLocation(listEntry, entityType, qtyCoupleOfAnimal, countEntityType, entityCharacteristicConfig, makeClassByReflection);
    }

    private void addNewEntityOnLocation(List<Entity> listEntry,
                                        EntityType entityType,
                                        int qtyCoupleOfAnimal,
                                        long countEntityType,
                                        EntityCharacteristicConfig entityCharacteristicConfig,
                                        MakeClassByReflection makeClassByReflection) {

        for (int i = 0; i < qtyCoupleOfAnimal; i++) {
            Entity newAnimal = makeClassByReflection.MakeClassByEntityType(entityType);
            int maxQtyOnCell = entityCharacteristicConfig.getCharacteristicMapConfig().get(entityType).getMaxQtyOnCell();
            if (countEntityType < maxQtyOnCell) {
                listEntry.add(newAnimal);
            }
        }
    }
}
