package com.xat.interview.base;

public class Dog extends Animal {

    public Dog(String name, int age) {
        //super(name, age);
    }

    public static void main(String[] args) {

        Animal animal = new Dog("小黑", 3);
        animal.sayHello("小黑");
    }
}
