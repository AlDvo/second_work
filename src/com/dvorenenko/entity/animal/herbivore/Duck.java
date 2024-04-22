package com.dvorenenko.entity.animal.herbivore;

import com.dvorenenko.entity.animal.abstracts.Herbivore;

public class Duck extends Herbivore {
    public Duck() {
    }

    public Duck(double weight, int speed, double meal, int maxQtyOnCell) {
        super(weight, speed, meal, maxQtyOnCell);
    }
}
