package com.xat.interview.thread;

/**
 * @author xuantao
 */
public class ThreadTest2 implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName()+"---"+i+"正在执行");
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            ThreadTest2 threadTest2 = new ThreadTest2();
            new Thread(threadTest2,"线程B").start();
            new Thread(threadTest2,"线程A").start();

        }


    }

}
