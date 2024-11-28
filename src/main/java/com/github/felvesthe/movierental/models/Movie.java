package com.github.felvesthe.movierental.models;

public class Movie {

    private final int id;
    private final String title;
    private final String director;

    public static int currentId = 1;

    public Movie(String title, String director) {
        this.id = currentId++;
        this.title = title;
        this.director = director;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    @Override
    public String toString() {
        return "Tytuł: " + title + "\nReżyser: " + director;
    }
}
