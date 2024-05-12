package com.dvorenenko.entity.animal.predator;

import com.dvorenenko.entity.animal.abstracts.Predator;

public class Wolf extends Predator {
    public Wolf() {
    }

    public Wolf(double weight, int speed, double meal, int maxQtyOnCell) {
        super(weight, speed, meal, maxQtyOnCell);
    }
}
