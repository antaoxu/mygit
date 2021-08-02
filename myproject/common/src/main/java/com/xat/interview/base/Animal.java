package com.xat.interview.base;

public class Animal {
    private String name;
    private int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Animal() {

    }

    public void sayHello(String name) {
        System.out.println(this.name + "在说话");
    }
}
