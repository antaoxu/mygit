package com.xat.leetCode;

/**
 * @author xuantao
 */
public class RemoveDuplicates {

    public static void main(String[] args) {
        int arr[] = {1, 2, 3, 3, 5, 6, 7, 8};
        System.out.println(removeDuplicates(arr));

    }

    public static int removeDuplicates(int[] arr) {
        int num = arr.length;
        int j = 0;
        for (int i = 0; i < num; i++) {
            if (arr[j] != arr[i]) {
                arr[++j] = arr[i];
            }
        }
        return j + 1;
    }
}
