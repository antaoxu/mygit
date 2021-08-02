package com.xat.interview.base;

import java.util.Arrays;

/**
 * @author xuantao
 */
public class ArrayTest {
    public static void main(String[] args) {
        int[] a = new int[5];
        a[0] = (int) (Math.random() * 100);
        a[1] = (int) (Math.random() * 100);
        a[2] = (int) (Math.random() * 100);
        a[3] = (int) (Math.random() * 100);
        a[4] = (int) (Math.random() * 100);

        System.out.println("原始数组:" + Arrays.toString(a));

        //数组反转
        //System.out.println("反转后的数组："+Arrays.toString(arrInverse(a)));
        //选择排序
        selectSort(a);


    }

    /**
     * 找出数组中的最小值
     *
     * @param arr
     */
    public static void minNum(int[] arr) {
        int min = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        System.out.println("数组中最小的数是：" + min);
    }

    /**
     * 数组反转
     */
    public static int[] arrInverse(int[] arr) {
        int[] inverseArr = new int[arr.length];
        for (int i = 0, j = arr.length - 1; i < arr.length; i++) {
            inverseArr[i] = arr[j];
            j--;
        }
        return inverseArr;
    }

    /**
     * 选择排序
     * 思路：1.从数组第一位开始，依次与后面的数进行比较，若比第一位的数小，则交换位置，
     * 2.接着从第二位执行步骤1，直到完成排序
     */
    public static void selectSort(int[] arr) {
        int[] targetArr = new int[arr.length];
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[i]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;

                }
            }
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println(" ");
    }


}
