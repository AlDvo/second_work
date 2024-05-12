package com.dvorenenko.entity.animal.predator;

import com.dvorenenko.entity.animal.abstracts.Predator;

public class Fox extends Predator {
    public Fox() {
    }

    public Fox(double weight, int speed, double meal, int maxQtyOnCell) {
        super(weight, speed, meal, maxQtyOnCell);
    }
}
