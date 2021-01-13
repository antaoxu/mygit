package com.xat.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
    public static void main(String[] args) {
        ApplicationContext context=new ClassPathXmlApplicationContext("beans1.xml");
        Hello hello= (Hello) context.getBean("hello");
        System.out.println(hello);
        System.out.println("dev分支");
        System.out.println("feature 分支");
    }
}
