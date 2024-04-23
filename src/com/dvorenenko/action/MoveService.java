package com.dvorenenko.action;

import com.dvorenenko.config.FieldSizeConfig;
import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.animal.abstracts.Animal;
import com.dvorenenko.entity.enums.DirectionType;
import com.dvorenenko.itteration.ChoseVariable;
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
        int maxQtyOnCell = entity.getMaxQtyOnCell();
        long count = listEntry.stream().filter(m -> m.getClass().equals(entity.getClass())).count();

        return maxQtyOnCell > count;
    }

    private Map<FieldSizeConfig, List<Entity>> makeLocationWithoutAnimal() {
        Map<FieldSizeConfig, List<Entity>> newLocation = new HashMap<>();

        for (int i = 0; i < ChoseVariable.LOCATION_HEIGHT; i++) {
            for (int j = 0; j < ChoseVariable.LOCATION_WEIGHT; j++) {
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
        boolean checkCanMoveUp = directionType == DirectionType.UP && fieldSizeConfig.getHeight() - entity.getSpeed() >= 0;
        boolean checkCanMoveDown = directionType == DirectionType.DOWN && fieldSizeConfig.getHeight() + entity.getSpeed() < ChoseVariable.LOCATION_HEIGHT;
        boolean checkCanMoveLeft = directionType == DirectionType.LEFT && fieldSizeConfig.getWeight() - entity.getSpeed() >= 0;
        boolean checkCanMoveRight = directionType == DirectionType.RIGHT && fieldSizeConfig.getWeight() + entity.getSpeed() < ChoseVariable.LOCATION_WEIGHT;

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
}
