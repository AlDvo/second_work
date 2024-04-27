package com.dvorenenko;

import com.dvorenenko.action.*;
import com.dvorenenko.config.EntityCharacteristicConfig;
import com.dvorenenko.config.FieldSizeConfig;
import com.dvorenenko.config.MakeClassByReflection;
import com.dvorenenko.config.PossibilityOfEatingConfig;
import com.dvorenenko.itteration.ChangeVariableService;
import com.dvorenenko.itteration.OneDaySimulationService;
import com.dvorenenko.itteration.StatisticsOfDay;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        FieldSizeConfig fieldSizeConfig = new FieldSizeConfig(ChangeVariableService.LOCATION_HEIGHT, ChangeVariableService.LOCATION_WEIGHT);
        Random random = new Random();
        ObjectMapper objectMapper = new ObjectMapper();
        MakeClassByReflection makeClassByReflection = new MakeClassByReflection();
        EntityCharacteristicConfig characteristicConfig = new EntityCharacteristicConfig(objectMapper, "resources/entity_characteristic.json", makeClassByReflection);
        PossibilityOfEatingConfig possibilityOfEatingConfig = new PossibilityOfEatingConfig(objectMapper, "resources/possibility_of_eating.json", makeClassByReflection);
        ChooseDirectionService chooseDirectionService = new ChooseDirectionService();
        EatingService eat = new EatingService();
        DecreaseAnimalService decreaseAnimalService = new DecreaseAnimalService();
        MultiplyService multiplyService = new MultiplyService();
        MoveService moveService = new MoveService();
        ChangeVariableService changeVariableService = new ChangeVariableService();
        StatisticsOfDay statistics = new StatisticsOfDay();



        OneDaySimulationService oneDaySimulationService = new OneDaySimulationService();
        oneDaySimulationService.passedOneDay(random,
                characteristicConfig,
                fieldSizeConfig,
                possibilityOfEatingConfig,
                decreaseAnimalService,
                eat,
                multiplyService,
                moveService,
                chooseDirectionService,
                makeClassByReflection,
                statistics);

    }
}
