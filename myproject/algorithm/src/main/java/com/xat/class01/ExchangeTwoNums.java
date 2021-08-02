package com.xat.class01;

import java.util.Random;

public class ExchangeTwoNums {
    /**
     * 不使用中间变量交换两个数
     * 思路：异或运算
     */
    public static void exchangeTwoNums(int a, int b) {
        //System.out.println(a+"   "+b);
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        //System.out.println(a+"   "+b);
    }

    public static void main(String[] args) {
        long a = System.currentTimeMillis();
        exchangeTwoNums(23, 12);
        exchangeTwoNums(23, 19);
        exchangeTwoNums(23, 14);
        long b = System.currentTimeMillis();
        System.out.println(a);
        System.out.println(b);

    }

}
