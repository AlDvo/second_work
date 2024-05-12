package com.dvorenenko.itteration;

import java.util.Scanner;

public class ChangeVariableService {
    public static int LOCATION_HEIGHT = 10;
    public static int LOCATION_WEIGHT = 10;
    public static int LOWER_LIMIT_SATURATION = 3;

    public ChangeVariableService() {
        userChangeVariable();
    }

    private void userChangeVariable(){
        System.out.println("Хотите указать свои переменные - y/n");
        Scanner scan =  new Scanner(System.in);
        String userChose = scan.nextLine();

        if(userChose.contains("y")){
            System.out.println("Укажите длинну локации");
            LOCATION_WEIGHT = scan.nextInt();
            System.out.println("Укажите ширину локации");
            LOCATION_HEIGHT = scan.nextInt();
            System.out.println("Укажите допустимое кол-во дней без еды");
            LOWER_LIMIT_SATURATION = scan.nextInt();
        }
    }
}

