package com.dvorenenko.itteration;

import com.dvorenenko.constants.Constants;

public class StatisticsOfDay {

    public void statisticsOfDay(int numberOfDay, int quantityEntityBeforeEat, int eatenAnimal, int countAnimalAfterMultiply, int countAnimalBeforeMultiply, int newQuantityEntityAfterMultiply) {
        System.out.println(Constants.START_DAY + (numberOfDay +1));
        System.out.println(Constants.QUANTITY_ANIMAL_IN_START_DAY + quantityEntityBeforeEat);
        System.out.println(Constants.QUANTITY_EATEN_ANIMAL + eatenAnimal);
        System.out.println(Constants.QUANTITY_BORN_ANIMAL + (countAnimalAfterMultiply - countAnimalBeforeMultiply));
        System.out.println(Constants.QUANTITY_ANIMAL_IN_END_DAY + newQuantityEntityAfterMultiply);
        System.out.println(Constants.DELIMETR_STATISTICS);
    }
}
