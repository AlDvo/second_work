package com.dvorenenko.entity.animal.predator;

import com.dvorenenko.entity.animal.abstracts.Predator;

public class Boa extends Predator {
    public Boa() {
    }

    public Boa(double weight, int speed, double meal, int maxQtyOnCell) {
        super(weight, speed, meal, maxQtyOnCell);
    }
}
