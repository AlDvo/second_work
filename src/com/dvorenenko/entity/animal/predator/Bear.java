package com.dvorenenko.entity.animal.predator;

import com.dvorenenko.entity.animal.abstracts.Predator;

public class Bear extends Predator {
    public Bear() {
    }

    public Bear(double weight, int speed, double meal, int maxQtyOnCell) {
        super(weight, speed, meal, maxQtyOnCell);
    }
}
