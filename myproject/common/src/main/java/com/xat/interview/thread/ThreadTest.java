package com.xat.interview.thread;

import java.util.concurrent.*;

/**
 * @author xuantao
 */
public class ThreadTest {
    static class MyThread extends Thread{
        @Override
        public void run(){
            System.out.println("通过继承Thread来创建的线程正在执行");
        }
    }

    static class MyRunnable implements Runnable{
        @Override
        public void run(){
            System.out.println("通过实现Runnable来创建的线程正在执行");
        }
    }

    static class MyCallable implements Callable<String>{
        @Override
        public String call() throws Exception {
            return "通过实现Callable来创建的线程正在执行";
        }
    }

    public static void main(String[] args) {
        //通过继承Thread来创建线程
        new MyThread().start();
        //通过实现Runnable接口来创建线程
        new Thread(new MyRunnable()).start();
        //通过Lamda表达式创建线程,{}中的内容即为run()方法的方法体
        new Thread(()->{
            System.out.println("通过Lamda表达式创建线程");
        }).start();
        //通过实现Callable接口来创建线程
        FutureTask<String> fu = new FutureTask<>(new MyCallable());
        new Thread(fu).start();
        //通过线程池来创建线程
        Executor executor = new ThreadPoolExecutor(5,20,60, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(5));
        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("通过线程池创建的线程正在执行");
            }
        });

    }
}
