package com.xat.interview.base;


public class StringToInt {

    public static void main(String[] args) {
        String string = "w";
        /*int anInt = Integer.parseInt(string);
        int num = Integer.valueOf(string);
        System.out.println(num);
        System.out.println(anInt);*/
        int num = Integer.valueOf(string);
        Integer a = 127;
        Integer b = 127;
        Integer c = 200;
        Integer d = 200;
        System.out.println(a == b);
        System.out.println(c == d);
        //System.out.println(a==b);
    }


}
