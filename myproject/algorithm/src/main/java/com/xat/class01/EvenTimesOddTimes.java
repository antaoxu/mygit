package com.xat.class01;

/**
 * 一个数组中找出出现奇数次、偶数次的数
 * @author xuantao
 * @date 2021/04/15
 */
public class EvenTimesOddTimes {
    /**
     * 1.一个数组中只有一种数出现了奇数次，其他都出现偶数次，请找出出现奇数次的数并打印。
     * 分析：采用异或的思想：相同为0，不同为1，也即无进位相加
     */
    public static void printOddTimesNum(int[] arr){
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        System.out.println(eor);
    }
    /**
     * 1.一个数组中有两种数出现了奇数次，其他都出现偶数次，请找出出现奇数次的数并打印。
     * 分析：(1)定义一个变量eor = 0，让其与数组中的数依次进行异或操作，得到结果eor = a^b
     *      (2)因为是两种数出现奇数次，则这两种数必然不相等，所以eor =a^b != 0,进而可推断出a与b
     *         必然存在一位或者多位不相等，根据该思路可将数组中的数分为两类，即某位为1的数和某位为0的数
     *      (3)选取不等的最右边一位，也即eor中为1的最右边一位，假设 a该位为1，则 b该位为0
     *      (4)再定义一个变量eor1 =0,让其与该数组中最右一位为1的数进行异或操作，得到a
     *      (5)最后 b = eor ^a =a^b^a =b
     */
    public static void printTwoOddTimesNmus(int[] arr){
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        //提取出最右的1
        int lastRightOne = eor & (~eor + 1);
        //int lastRightOne = eor &(-eor);
        //再定义一个变量eor1 =0
        int oneOddNum = 0;
        for (int i = 0; i < arr.length; i++) {
            if ((arr[i]&lastRightOne)!= 0){
                oneOddNum ^= arr[i];
            }
        }
        int otherOddNum = eor ^ oneOddNum;
        System.out.println("数组中出现了奇数次的其中一个数为："+oneOddNum+"另一个数为："+otherOddNum);
    }

    public static int bit1counts(int N) {
        int count = 0;

        //   011011010000
        //   000000010000     1
        //   011011000000
        //
        while(N != 0) {
            int rightOne = N & ((~N) + 1);
            count++;
            N ^= rightOne;
            // N -= rightOne
            System.out.println(count+"  N:"+N);
        }

        return count;


    }



    public static void main(String[] args) {
        int [] arr = {1,2,2,3,3,4,4,5,5,4,4,4,4,5,5,5,5,5,5,7,7,8};
        //printOddTimesNum(arr);
        //printTwoOddTimesNmus(arr);
        bit1counts(7);
    }
}
