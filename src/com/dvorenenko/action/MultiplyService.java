package com.dvorenenko.action;

import com.dvorenenko.config.EntityCharacteristicConfig;
import com.dvorenenko.config.FieldSizeConfig;
import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.enums.EntityType;
import com.dvorenenko.location.Location;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class MultiplyService {

    public Location multiplynimalOnLocation(Location location, EntityCharacteristicConfig entityCharacteristicConfig) {

        for (Map.Entry<FieldSizeConfig, List<Entity>> listEntry : location.getIsland().entrySet()) {
            for (EntityType entityType : EntityType.values()) {
                if (entityType != EntityType.GRASS) {
                    countCoupleOfAnimalAndAddChild(entityCharacteristicConfig, listEntry.getValue(), entityType);
                }else{
                    multiplyGrass(entityCharacteristicConfig, listEntry, entityType);
                }
            }
        }
        return location;
    }

    private void multiplyGrass(EntityCharacteristicConfig entityCharacteristicConfig, Map.Entry<FieldSizeConfig, List<Entity>> listEntry, EntityType entityType) {
        Class type = entityType.getClazz();
        int countEntityType = (int) listEntry.getValue().stream().filter(type::isInstance).count();
        addNewEntityOnLocation(listEntry.getValue(), entityType,countEntityType,countEntityType, entityCharacteristicConfig);
    }

    private void countCoupleOfAnimalAndAddChild(EntityCharacteristicConfig entityCharacteristicConfig,
                                                List<Entity> listEntry,
                                                EntityType entityType) {
        Class type = entityType.getClazz();
        long countEntityType = listEntry.stream().filter(type::isInstance).count();
        int qtyCoupleOfAnimal = (int) (countEntityType / 2);
        addNewEntityOnLocation(listEntry, entityType, qtyCoupleOfAnimal, countEntityType, entityCharacteristicConfig);
    }

    private void addNewEntityOnLocation(List<Entity> listEntry,
                                        EntityType entityType,
                                        int qtyCoupleOfAnimal,
                                        long countEntityType,
                                        EntityCharacteristicConfig entityCharacteristicConfig) {

        for (int i = 0; i < qtyCoupleOfAnimal; i++) {
            Entity newAnimal = getEntityForMultiply(entityType);
            int maxQtyOnCell = entityCharacteristicConfig.getCharacteristicMapConfig().get(entityType).getMaxQtyOnCell();
            if (countEntityType < maxQtyOnCell) {
                listEntry.add(newAnimal);
            }
        }
    }


    private Entity getEntityForMultiply(EntityType entityType) {
        Entity entityForMultiply;
        try {
            entityForMultiply = (Entity) entityType.getClazz().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return entityForMultiply;
    }
}
