package com.dvorenenko.itteration;

import com.dvorenenko.constants.Constants;

import java.util.Scanner;

public class ChoseQtyDay {

    private int day;

    public ChoseQtyDay() {
        this.day = choseDay();
    }

    public int getDay() {
        return day;
    }
    private int choseDay(){
        System.out.println(Constants.ENTER_QTY_DAYS);
        Scanner scan = new Scanner(System.in);
        return scan.nextInt();
    }
}
