package com.dvorenenko;

import com.dvorenenko.action.*;
import com.dvorenenko.config.EntityCharacteristicConfig;
import com.dvorenenko.config.FieldSizeConfig;
import com.dvorenenko.config.PossibilityOfEatingConfig;
import com.dvorenenko.itteration.ChoseVariable;
import com.dvorenenko.itteration.OneDay;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        FieldSizeConfig fieldSizeConfig = new FieldSizeConfig(ChoseVariable.LOCATION_HEIGHT, ChoseVariable.LOCATION_WEIGHT);
        Random random = new Random();
        ObjectMapper objectMapper = new ObjectMapper();
        EntityCharacteristicConfig characteristicConfig = new EntityCharacteristicConfig(objectMapper, "resources/entity_characteristic.json");
        PossibilityOfEatingConfig possibilityOfEatingConfig = new PossibilityOfEatingConfig(objectMapper, "resources/possibility_of_eating.json");
        ChooseDirectionService chooseDirectionService = new ChooseDirectionService();
        EatingService eat = new EatingService();
        DecreaseAnimalService decreaseAnimalService = new DecreaseAnimalService();
        MultiplyService multiplyService = new MultiplyService();
        MoveService moveService = new MoveService();
        ChoseVariable choseVariable = new ChoseVariable();



        OneDay oneDay = new OneDay();
        oneDay.passedOneDay(random,
                characteristicConfig,
                fieldSizeConfig,
                possibilityOfEatingConfig,
                decreaseAnimalService,
                eat,
                multiplyService,
                moveService,
                chooseDirectionService);

    }
}
