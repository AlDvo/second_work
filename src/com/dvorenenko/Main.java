package com.dvorenenko;

import com.dvorenenko.action.ChooseDirectionService;
import com.dvorenenko.action.EatingService;
import com.dvorenenko.action.MoveService;
import com.dvorenenko.config.EntityCharacteristicConfig;
import com.dvorenenko.config.FieldSizeConfig;
import com.dvorenenko.config.PossibilityOfEatingConfig;
import com.dvorenenko.constants.Constants;
import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.animal.herbivore.*;
import com.dvorenenko.entity.animal.predator.Bear;
import com.dvorenenko.entity.animal.predator.Eagle;
import com.dvorenenko.entity.grass.Grass;
import com.dvorenenko.location.Location;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        FieldSizeConfig fieldSizeConfig = new FieldSizeConfig(Constants.LOCATION_HEIGHT, Constants.LOCATION_WEIGHT);
        Random random = new Random();
        ObjectMapper objectMapper = new ObjectMapper();
        EntityCharacteristicConfig characteristicConfig = new EntityCharacteristicConfig(objectMapper, "resources/entity_characteristic.json");
        ChooseDirectionService chooseDirectionService = new ChooseDirectionService();

        Location location = new Location(random, characteristicConfig, fieldSizeConfig);

        MoveService moveService = new MoveService();
        Location location1 = moveService.move(location, random, chooseDirectionService);

        for (Map.Entry<FieldSizeConfig, List<Entity>> fieldSizeConfigListEntry : location1.getIsland().entrySet()) {
            System.out.println(fieldSizeConfigListEntry.getValue().stream().filter(m -> m.getClass().equals(Grass.class) ).count());
        }

        PossibilityOfEatingConfig poec = new PossibilityOfEatingConfig(objectMapper, "resources/possibility_of_eating.json");

        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++=");
        EatingService eat = new EatingService();
        Location eat1 = eat.eat(location1, random, characteristicConfig, poec);

        for (Map.Entry<FieldSizeConfig, List<Entity>> fieldSizeConfigListEntry : eat1.getIsland().entrySet()) {
            System.out.println(fieldSizeConfigListEntry.getValue().stream().filter(m -> m.getClass().equals(Grass.class) ).count());
        }
    }
}
