package com.dvorenenko.entity.grass;

import com.dvorenenko.entity.Entity;

public class Grass extends Entity {
    public Grass() {
    }

    public Grass(double weight, int speed, double meal, int maxQtyOnCell) {
        super(weight, speed, meal, maxQtyOnCell);
    }
}
