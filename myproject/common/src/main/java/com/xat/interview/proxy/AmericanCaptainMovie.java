package com.xat.interview.proxy;

/**
 * @author xuantao
 * 具体实现类
 */
public class AmericanCaptainMovie implements Movie {
    @Override
    public void play() {
        System.out.println("影院正在播放美国队长");
    }
}
