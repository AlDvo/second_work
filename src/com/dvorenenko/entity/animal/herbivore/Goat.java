package com.dvorenenko.entity.animal.herbivore;

import com.dvorenenko.entity.animal.abstracts.Herbivore;

public class Goat extends Herbivore {
    public Goat() {
    }

    public Goat(double weight, int speed, double meal, int maxQtyOnCell) {
        super(weight, speed, meal, maxQtyOnCell);
    }
}
