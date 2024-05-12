package com.dvorenenko.action;

import com.dvorenenko.config.FieldSizeConfig;
import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.animal.abstracts.Animal;
import com.dvorenenko.entity.enums.DirectionType;
import com.dvorenenko.itteration.ChangeVariableService;
import com.dvorenenko.location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MoveService {

    public Location move(Location location, Random random, ChooseDirectionService chooseDirectionService) {
        Map<FieldSizeConfig, List<Entity>> newLocation = makeLocationWithoutAnimal();

        for (Map.Entry<FieldSizeConfig, List<Entity>> listEntry : location.getIsland().entrySet()) {
            for (Entity entity : listEntry.getValue()) {
                DirectionType chooseDirection = chooseDirectionService.chooseDirection(random);

                if (checkCanAnimalMoveInLocation(listEntry.getKey(), entity, chooseDirection)) {
                    FieldSizeConfig newKey = newKeyAfterMoveEntity(chooseDirection, listEntry, entity);
                    newLocation.get(newKey).add(entity);
                } else {
                    newLocation.get(listEntry.getKey()).add(entity);
                }
            }
        }
        return new Location(newLocation);
    }

    private boolean checkLimitOfAnimalInCell(List<Entity> listEntry, Entity entity) {
        int maxQuantityOnCell = entity.getMaxQuantityOnCell();
        long count = listEntry.stream().
                filter(m -> m.getClass().
                        equals(entity.getClass())).count();

        return maxQuantityOnCell > count;
    }

    private Map<FieldSizeConfig, List<Entity>> makeLocationWithoutAnimal() {
        Map<FieldSizeConfig, List<Entity>> newLocation = new HashMap<>();

        for (int i = 0; i < ChangeVariableService.LOCATION_HEIGHT; i++) {
            for (int j = 0; j < ChangeVariableService.LOCATION_WEIGHT; j++) {
                newLocation.put(new FieldSizeConfig(i, j), new ArrayList<>());
            }
        }
        return newLocation;
    }

    private FieldSizeConfig newKeyAfterMoveEntity(DirectionType directionType,
                                                  Map.Entry<FieldSizeConfig, List<Entity>> listEntry,
                                                  Entity entity) {

        if (directionType == DirectionType.DOWN && checkLimitOfAnimalInCell(listEntry.getValue(), entity)) {
            return new FieldSizeConfig(listEntry.getKey().getHeight() + entity.getSpeed(), listEntry.getKey().getWeight());
        } else if (directionType == DirectionType.UP && checkLimitOfAnimalInCell(listEntry.getValue(), entity)) {
            return new FieldSizeConfig(listEntry.getKey().getHeight() - entity.getSpeed(), listEntry.getKey().getWeight());
        } else if (directionType == DirectionType.LEFT && checkLimitOfAnimalInCell(listEntry.getValue(), entity)) {
            return new FieldSizeConfig(listEntry.getKey().getHeight(), listEntry.getKey().getWeight() - entity.getSpeed());
        } else if (directionType == DirectionType.RIGHT && checkLimitOfAnimalInCell(listEntry.getValue(), entity)) {
            return new FieldSizeConfig(listEntry.getKey().getHeight(), listEntry.getKey().getWeight() + entity.getSpeed());
        }
        return new FieldSizeConfig(listEntry.getKey().getHeight(), listEntry.getKey().getWeight());
    }

    private boolean checkCanAnimalMoveInLocation(FieldSizeConfig fieldSizeConfig,
                                                 Entity entity,
                                                 DirectionType directionType) {

        boolean animalWhoNotMoveYet = entity instanceof Animal && entity.isAlive();
        boolean checkCanMoveUp = isCheckCanMoveUp(fieldSizeConfig, entity, directionType);
        boolean checkCanMoveDown = isCheckCanMoveDown(fieldSizeConfig, entity, directionType);
        boolean checkCanMoveLeft = isCheckCanMoveLeft(fieldSizeConfig, entity, directionType);
        boolean checkCanMoveRight = isCheckCanMoveRight(fieldSizeConfig, entity, directionType);

        if (animalWhoNotMoveYet && checkCanMoveUp) {
            return true;
        } else if (animalWhoNotMoveYet && checkCanMoveDown) {
            return true;
        } else if (animalWhoNotMoveYet && checkCanMoveLeft) {
            return true;
        } else if (animalWhoNotMoveYet && checkCanMoveRight) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isCheckCanMoveRight(FieldSizeConfig fieldSizeConfig, Entity entity, DirectionType directionType) {
        return directionType == DirectionType.RIGHT && fieldSizeConfig.getWeight() + entity.getSpeed() < ChangeVariableService.LOCATION_WEIGHT;
    }

    private boolean isCheckCanMoveLeft(FieldSizeConfig fieldSizeConfig, Entity entity, DirectionType directionType) {
        return directionType == DirectionType.LEFT && fieldSizeConfig.getWeight() - entity.getSpeed() >= 0;
    }

    private boolean isCheckCanMoveDown(FieldSizeConfig fieldSizeConfig, Entity entity, DirectionType directionType) {
        return directionType == DirectionType.DOWN && fieldSizeConfig.getHeight() + entity.getSpeed() < ChangeVariableService.LOCATION_HEIGHT;
    }

    private boolean isCheckCanMoveUp(FieldSizeConfig fieldSizeConfig, Entity entity, DirectionType directionType) {
        return directionType == DirectionType.UP && fieldSizeConfig.getHeight() - entity.getSpeed() >= 0;
    }
}
