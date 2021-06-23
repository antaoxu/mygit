package com.xat.sofaboot.service;

/**
 * @author xuantao
 */
public class HelloServiceImpl implements HelloService{
    @Override
    public String sayHello(String string){
        System.out.println("server received "+string);
        return "hello"+string+"!";
    }
}
