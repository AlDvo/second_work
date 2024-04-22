package com.dvorenenko.entity.animal.herbivore;

import com.dvorenenko.entity.animal.abstracts.Herbivore;

public class Boar extends Herbivore {
    public Boar() {
    }

    public Boar(double weight, int speed, double meal, int maxQtyOnCell) {
        super(weight, speed, meal, maxQtyOnCell);
    }
}
