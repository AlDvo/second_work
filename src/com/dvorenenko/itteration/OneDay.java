package com.dvorenenko.itteration;

import com.dvorenenko.action.ChooseDirectionService;
import com.dvorenenko.action.DecreaseAnimalService;
import com.dvorenenko.action.EatingService;
import com.dvorenenko.action.MoveService;
import com.dvorenenko.action.MultiplyService;
import com.dvorenenko.config.EntityCharacteristicConfig;
import com.dvorenenko.config.FieldSizeConfig;
import com.dvorenenko.config.PossibilityOfEatingConfig;
import com.dvorenenko.location.Location;

import java.util.Random;

public class OneDay {
    private int day;

    public OneDay() {
        this.day = getDay();
    }

    public int getDay() {
        ChoseQtyDay day = new ChoseQtyDay();
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
                             ChooseDirectionService chooseDirectionService) {

        Location location = new Location(random, characteristicConfig, fieldSizeConfig);

        for (int i = 0; i < day; i++) {
            location = eat.eat(location, random, possibilityOfEatingConfig);
            location = decreaseAnimalService.decreaseAnimalOnLocation(location);
            location = multiplyService.multiplynimalOnLocation(location, characteristicConfig);
            location = moveService.move(location, random, chooseDirectionService);
        }
    }

}
