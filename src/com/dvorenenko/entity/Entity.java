package com.dvorenenko.entity;

public abstract class Entity {

    protected double weight;
    protected int speed;
    protected double mealKg;
    protected int maxQuantityOnCell;
    protected boolean alive = true;
    protected int countOfHunger = 0;

    protected Entity(double weight, int speed, double meal, int maxQuantityOnCell) {
        this.weight = weight;
        this.speed = speed;
        this.mealKg = meal;
        this.maxQuantityOnCell = maxQuantityOnCell;
    }

    protected Entity() {

    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public double getWeight() {
        return weight;
    }

    public int getSpeed() {
        return speed;
    }

    public double getMealKg() {
        return mealKg;
    }

    public int getMaxQuantityOnCell() {
        return maxQuantityOnCell;
    }

    public void setMealKg(double mealKg) {
        this.mealKg = mealKg;
    }

    public int getCountOfHunger() {
        return countOfHunger;
    }

    public void setCountOfHunger(int countOfHunger) {
        this.countOfHunger = countOfHunger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;
        return Double.compare(weight, entity.weight) == 0 && speed == entity.speed && Double.compare(mealKg, entity.mealKg) == 0 && maxQuantityOnCell == entity.maxQuantityOnCell;
    }

    @Override
    public int hashCode() {
        int result = Double.hashCode(weight);
        result = 31 * result + speed;
        result = 31 * result + Double.hashCode(mealKg);
        result = 31 * result + maxQuantityOnCell;
        return result;
    }
}
