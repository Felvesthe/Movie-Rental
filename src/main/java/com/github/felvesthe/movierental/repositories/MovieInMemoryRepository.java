package com.github.felvesthe.movierental.repositories;

import com.github.felvesthe.movierental.models.Movie;
import com.github.felvesthe.movierental.repositories.interfaces.MovieRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieInMemoryRepository implements MovieRepository {

    public static final List<Movie> movies = new ArrayList<>();

    @Override
    public Optional<Movie> findById(int id) {
        return movies
                .stream()
                .filter(movie -> movie.getId() == id)
                .findFirst();
    }

    @Override
    public boolean isExists(String title) {
        return movies
                .stream()
                .anyMatch(movie -> movie.getTitle().equals(title));
    }

    @Override
    public Movie save(Movie movie) {
        movies.add(movie);

        return movie;
    }

    @Override
    public List<Movie> findAll() {
        return movies;
    }
}
