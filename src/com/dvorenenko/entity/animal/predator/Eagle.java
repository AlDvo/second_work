package com.dvorenenko.entity.animal.predator;

import com.dvorenenko.entity.animal.abstracts.Predator;

public class Eagle extends Predator {
    public Eagle() {
    }

    public Eagle(double weight, int speed, double meal, int maxQtyOnCell) {
        super(weight, speed, meal, maxQtyOnCell);
    }
}
