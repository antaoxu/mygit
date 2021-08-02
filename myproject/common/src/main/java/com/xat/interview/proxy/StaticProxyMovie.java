package com.xat.interview.proxy;

public class StaticProxyMovie implements Movie {
    Movie movie;

    public StaticProxyMovie() {
    }

    public StaticProxyMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public void play() {
        beforPlay();
        movie.play();
        stopPlay();
        afterPlay();
    }

    private void stopPlay() {
        System.out.println("播放暂停");
    }

    private void afterPlay() {
        System.out.println("电影放映结束---");
    }

    private void beforPlay() {
        System.out.println("电影放映开始---");
    }
}
