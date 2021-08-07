package com.xat.class01;

import java.util.HashMap;

/**
 * @author 淡漠
 * @Date 20210807
 * 一个数组中有一种数出现K次，其他数都出现了M次，
 * M > 1,  K < M
 * 找到，出现了K次的数，
 * 要求，额外空间复杂度O(1)，时间复杂度O(N)
 */
public class KtimesAndMtimes {

    /**
     * 分析：1）将所有的数转换成二进制按位相加得到一个32位的整型数组int[] temp;
     *      2）从第0位开始依次对M取模,若arr[i]%M == 0,说明出现K次的数在 i位置为 0,否则为 1;
     *      3）定义初始变量int result = 0,将第二步中 i位置的 1提取到result对应的 i位置,
     *         从而得到result，也就是出现了K次的数.
     * @param arr 输入的满足条件的数组
     * @param k   k 次
     * @param m   M次
     * @return    出现K次的数
     */
    public static int onlyKtimes(int[] arr,int k, int m){
        int[] temp = new int[32];
        for (int num :arr){
            for (int i = 0; i < 32; i++) {
                //num 第i位上为1，则temp数组中i位置的数就加1
                /*if (((num >> i) & 1) !=0){
                    temp[i]++;
                }*/
                if ((num &(1<<i)) != 0){
                    temp[i]++;
                }
            }
        }
        int result = 0;
        for (int i = 0; i < 32; i++) {
            if ((temp[i] % m) != 0){
                result |= 1<< i;
            }
        }
        return result;
    }

    /**
     * 方法二：用 Hash表来实现
     * @param arr
     * @param k
     * @param m
     * @return
     */
    public static int anotherOnlyKtimes(int[] arr, int k ,int m){
        HashMap<Integer,Integer> map = new HashMap<Integer, Integer>();
        //统计arr中每个数出现的频率,将arr中的数作为map中的key,出现的次数为map的value
        for(int num: arr){
            if (map.containsKey(num)){
                map.put(num,map.get(num)+1);
            }else {
                map.put(num,1);
            }
        }
        //遍历map中的key,如果出现频率为K,直接返回
        /*for (int num: map.keySet()) {
            if (map.get(num) == k){
                return num;
            }
        }*/

        for (int i = 0; i < arr.length; i++) {
            if (map.get(arr[i]) == k){
                return arr[i];
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] arr = {-5,10,10,10,-199,34,34,-199,34,23,-5,23,23,2,2,2,234,234,234,-199};
        System.out.println(onlyKtimes(arr, 2, 3));
        //System.out.println(anotherOnlyKtimes(arr,2,3));
    }

}
