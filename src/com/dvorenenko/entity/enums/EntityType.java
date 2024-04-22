package com.dvorenenko.entity.enums;

import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.animal.herbivore.*;
import com.dvorenenko.entity.animal.predator.Bear;
import com.dvorenenko.entity.animal.predator.Eagle;
import com.dvorenenko.entity.animal.predator.Fox;
import com.dvorenenko.entity.animal.predator.Wolf;
import com.dvorenenko.entity.grass.Grass;

public enum EntityType {
    GRASS("grass", Grass.class),
    BOAR("boar" , Boar.class),
    BUFFALO("buffalo", Buffalo.class),
    CATERPILLAR("caterpillar", Caterpillar.class),
    DEER("deer", Deer.class),
    DUCK("duck", Duck.class),
    GOAT("goat", Goat.class),
    HORSE("horse", Horse.class),
    MOUSE("mouse", Mouse.class),
    RABBIT("rabbit", Rabbit.class),
    SHEEP("sheep", Sheep.class),
    BEAR("bear", Bear.class),
    BOA("boa", Boar.class),
    EAGLE("eagle", Eagle.class),
    FOX("fox", Fox.class),
    WOLF("wolf" , Wolf.class);

    private String type;
    private Class clazz;

    EntityType(String type, Class clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    public String getType() {
        return type;
    }

    public Class getClazz() {
        return clazz;
    }

    EntityType(String type) {
        this.type = type;
    }
}
