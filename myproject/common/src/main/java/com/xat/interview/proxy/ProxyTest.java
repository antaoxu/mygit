package com.xat.interview.proxy;

/**
 * @author xuantao
 */
public class ProxyTest {
    public static void main(String[] args) {
        Movie americanCaptainMovie = new AmericanCaptainMovie();
        Movie staticProxyMovie = new StaticProxyMovie(americanCaptainMovie);
        staticProxyMovie.play();

    }


}
