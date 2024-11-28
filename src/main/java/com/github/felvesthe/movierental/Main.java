package com.github.felvesthe.movierental;

public class Main {

    public static void main(String[] args) {
        App.initializeUsers();
        App.initializeMovies();
        App.run();
    }
}