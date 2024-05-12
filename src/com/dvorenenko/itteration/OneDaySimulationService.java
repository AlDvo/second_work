package com.dvorenenko.itteration;

import com.dvorenenko.action.ChooseDirectionService;
import com.dvorenenko.action.DecreaseAnimalService;
import com.dvorenenko.action.EatingService;
import com.dvorenenko.action.MoveService;
import com.dvorenenko.action.MultiplyService;
import com.dvorenenko.config.EntityCharacteristicConfig;
import com.dvorenenko.config.FieldSizeConfig;
import com.dvorenenko.config.MakeClassByReflection;
import com.dvorenenko.config.PossibilityOfEatingConfig;
import com.dvorenenko.constants.Constants;
import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.animal.abstracts.Animal;
import com.dvorenenko.location.Location;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class OneDaySimulationService {
    private int day;

    public OneDaySimulationService() {
        this.day = getDay();
    }

    public int getDay() {
        ChooseQtyDayService day = new ChooseQtyDayService();
        return day.getDay();
    }

    public void passedOneDay(Random random,
                             EntityCharacteristicConfig characteristicConfig,
                             FieldSizeConfig fieldSizeConfig,
                             PossibilityOfEatingConfig possibilityOfEatingConfig,
                             DecreaseAnimalService decreaseAnimalService,
                             EatingService eat,
                             MultiplyService multiplyService,
                             MoveService moveService,
                             ChooseDirectionService chooseDirectionService,
                             MakeClassByReflection makeClassByReflection,
                             StatisticsOfDay statistics) {

        Location location = new Location(random, characteristicConfig, fieldSizeConfig);

        for (int i = 0; i < day; i++) {
            int quantityEntityBeforeEat = countAnimal(location);

            location = eat.eat(location, random, possibilityOfEatingConfig, makeClassByReflection, characteristicConfig);
            int eatenAnimal = countEatenAnimal(location);

            location = decreaseAnimalService.decreaseAnimalOnLocation(location);
            int countAnimalBeforeMultiply = countAnimal(location);

            location = multiplyService.multiplynimalOnLocation(location, characteristicConfig, makeClassByReflection);
            int countAnimalAfterMultiply = countAnimal(location);

            location = moveService.move(location, random, chooseDirectionService);
            int newQuantityEntityAfterMultiply = countAnimal(location);


            statistics.statisticsOfDay(i, quantityEntityBeforeEat, eatenAnimal, countAnimalAfterMultiply, countAnimalBeforeMultiply, newQuantityEntityAfterMultiply);

        }
    }

    private int countAnimal(Location location) {
        int quantityEntity = 0;
        for (Map.Entry<FieldSizeConfig, List<Entity>> fieldSizeConfigListEntry : location.getIsland().entrySet()) {
            long countEntity = fieldSizeConfigListEntry.getValue().stream().filter(m -> m instanceof Animal).count();
            quantityEntity += (int) countEntity;
        }
        return quantityEntity;
    }

    private int countEatenAnimal(Location location) {
        int eaten = 0;
        for (Map.Entry<FieldSizeConfig, List<Entity>> fieldSizeConfigListEntry : location.getIsland().entrySet()) {
            long countEatenOnFieldSizeConfig = fieldSizeConfigListEntry.getValue().stream().filter(m -> !m.isAlive()).filter(m -> m instanceof Animal).count();
            eaten += (int) countEatenOnFieldSizeConfig;
        }
        return eaten;
    }

}
