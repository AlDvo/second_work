package com.dvorenenko.entity.animal.herbivore;

import com.dvorenenko.entity.animal.abstracts.Herbivore;

public class Horse extends Herbivore {
    public Horse() {
    }

    public Horse(double weight, int speed, double meal, int maxQtyOnCell) {
        super(weight, speed, meal, maxQtyOnCell);
    }
}
