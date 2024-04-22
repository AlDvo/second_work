package com.dvorenenko.entity.animal.abstracts;

import com.dvorenenko.entity.grass.Grass;

public abstract class Herbivore extends Animal {
    public Herbivore() {
    }

    protected Herbivore(double weight, int speed, double meal, int maxQtyOnCell) {
        super(weight, speed, meal, maxQtyOnCell);
    }
}
