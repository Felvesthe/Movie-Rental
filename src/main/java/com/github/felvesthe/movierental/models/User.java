package com.github.felvesthe.movierental.models;

import com.github.felvesthe.movierental.records.RentedMovie;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final String username;
    private final String email;
    private final String password;
    private final List<RentedMovie> rentedMovies;
    private boolean isLogged = false;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.rentedMovies = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<RentedMovie> getRentedMovies() {
        return rentedMovies;
    }

    public boolean hasMovie(Movie movie) {
        return rentedMovies
                .stream()
                .anyMatch(rentedMovie -> rentedMovie.movie().equals(movie));
    }

    public void addRentedMovie(RentedMovie rentedMovie) {
        rentedMovies.add(rentedMovie);
    }

    public void removeRentedMovie(int movieId) {
        rentedMovies.remove(movieId);
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void login() {
        isLogged = true;
    }
}
