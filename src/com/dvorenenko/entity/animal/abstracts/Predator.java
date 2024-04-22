package com.dvorenenko.entity.animal.abstracts;

public abstract class Predator extends Animal {

    protected Predator(double weight, int speed, double meal, int maxQtyOnCell) {
        super(weight, speed, meal, maxQtyOnCell);
    }

    public Predator() {
    }
}
