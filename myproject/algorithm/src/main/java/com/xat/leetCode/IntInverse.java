package com.xat.leetCode;

public class IntInverse {
    /**
     * 整数反转
     * 给你一个 32 位的有符号整数 x ，返回将 x 中的数字部分反转后的结果。
     * 如果反转后整数超过 32 位的有符号整数的范围 [−2*31,  2*31 − 1] ，就返回 0。
     */
    public static int reverse(int x) {
        int ans = 0;
        while (x != 0) {
            ans = ans * 10 + x % 10;
            x = x / 10;

        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(reverse(1138478798));
    }
}
