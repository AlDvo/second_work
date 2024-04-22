package com.dvorenenko.action;

import com.dvorenenko.entity.enums.DirectionType;

import java.util.Random;

public class ChooseDirectionService {

    public DirectionType chooseDirection(Random random){
        return DirectionType.values()[random.nextInt(DirectionType.values().length)];
    }
}
