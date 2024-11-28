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
        for (Movie movie : movies) {
            if (movie.getId() == id) {
                return Optional.of(movie);
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean isExists(String title) {
        for (Movie movie : movies) {
            if (movie.getTitle().equals(title)) {
                return true;
            }
        }

        return false;
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
