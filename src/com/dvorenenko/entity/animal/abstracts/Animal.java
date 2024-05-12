package com.dvorenenko.entity.animal.abstracts;

import com.dvorenenko.entity.Entity;

public abstract class Animal extends Entity {

    public Animal() {
    }

    protected Animal(double weight, int speed, double meal, int maxQtyOnCell) {
        super(weight, speed, meal, maxQtyOnCell);
    }
}
