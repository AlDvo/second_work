package com.dvorenenko.config;

public class FieldSizeConfig {
    private int height;
    private int weight;

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public FieldSizeConfig(int height, int weight) {
        this.height = height;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "FieldSizeConfig{" +
                "height=" + height +
                ", weight=" + weight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldSizeConfig that = (FieldSizeConfig) o;
        return height == that.height && weight == that.weight;
    }

    @Override
    public int hashCode() {
        int result = height;
        result = 31 * result + weight;
        return result;
    }
}
