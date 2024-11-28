package com.github.felvesthe.movierental.repositories.interfaces;

import com.github.felvesthe.movierental.models.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {

    Optional<Movie> findById(int id);
    boolean isExists(String title);
    Movie save(Movie movie);
    List<Movie> findAll();
}
