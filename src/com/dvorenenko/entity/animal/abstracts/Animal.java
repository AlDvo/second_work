package com.dvorenenko.entity.animal.abstracts;

import com.dvorenenko.action.AnimalAction;
import com.dvorenenko.entity.Entity;

public abstract class Animal extends Entity implements AnimalAction {

    public Animal() {
    }

    protected Animal(double weight, int speed, double meal, int maxQtyOnCell) {
        super(weight, speed, meal, maxQtyOnCell);
    }
    @Override
    public void eating() {

    }

    @Override
    public void multiply() {

    }

    @Override
    public void chooseDirection() {

    }
}
