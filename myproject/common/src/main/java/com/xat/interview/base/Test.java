package com.xat.interview.base;

import java.io.File;


public class Test {
    public static void main(String[] args) {
        String path = "CHECKFILE/20210216/UACC_307034_consumer_20210216.txt";
        String okPath = path.substring(0, path.lastIndexOf(".")) + ".ok";
        int index = okPath.lastIndexOf("/");

        String fileName = path.substring(index + 1);
        int dex = fileName.indexOf("_");
        String okFile = fileName.substring(dex + 1);
        File file = new File(fileName);


        System.out.println(okPath);
        System.out.println(fileName);
    }

}
