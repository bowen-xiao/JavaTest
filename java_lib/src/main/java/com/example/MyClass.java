package com.example;

import java.net.URLDecoder;
import java.util.Calendar;

public class MyClass {

    private String mTimeV;
    private String mDateV;
    private String mYearV;

    public static void main(String[] args){
        String saleDays = "saleDates=2017-07-25%2C2017-07-04%2C2017-07-10";
        System.out.println(URLDecoder.decode(saleDays));
        Calendar instance = Calendar.getInstance();
        System.out.println(instance.get(Calendar.MONTH) + " month");
        System.out.println(instance.get(Calendar.HOUR_OF_DAY) + " HOUR_OF_DAY");
        for (int i = 0; i < 10; i++) {
            System.out.println(i +"---");
            switch (i){
                case 6 :
                    System.out.println("6");
                    continue;
                case 1 :
                    System.out.println("111");
                    break;
            }
            if(i == 7){
                break;
            }
        }
        String testCitys= "122122|41111|jhkjdfj";
        System.out.println(testCitys.replaceAll("\\|","、"));
    }

    private void setAge(String age){
        boolean result  = getMatch(age);
    }

    private boolean getMatch(String age) {
        return age.matches("\\d.\\-*");
    }

}
