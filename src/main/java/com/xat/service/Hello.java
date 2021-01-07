package com.xat.service;


/**
 * @author xuantao
 * @version 1.8
 * @since 1.8
 * @return
 * @param 
 *
 */

public class Hello {
    private  String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "Hello{" +
                "str='" + str + '\'' +
                '}';
    }
}
