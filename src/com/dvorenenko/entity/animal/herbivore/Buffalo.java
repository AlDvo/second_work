package com.dvorenenko.entity.animal.herbivore;

import com.dvorenenko.entity.animal.abstracts.Herbivore;

public class Buffalo extends Herbivore {
    public Buffalo() {
    }

    public Buffalo(double weight, int speed, double meal, int maxQtyOnCell) {
        super(weight, speed, meal, maxQtyOnCell);
    }
}
