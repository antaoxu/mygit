package com.xat.interview.thread;

/**
 * @author xuantao
 */
public class ThreadTest1 extends Thread {
    @Override
    public void run(){
        System.out.println(Thread.currentThread().getName()+"正在执行");
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            ThreadTest1 threadTest1 = new ThreadTest1();
            threadTest1.start();
            //threadTest1.run();
        }
        System.out.println("main线程正在执行");

    }

}
