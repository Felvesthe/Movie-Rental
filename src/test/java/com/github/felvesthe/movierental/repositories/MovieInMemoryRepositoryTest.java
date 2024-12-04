package com.github.felvesthe.movierental.repositories;

import com.github.felvesthe.movierental.models.Movie;
import com.github.felvesthe.movierental.repositories.interfaces.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MovieInMemoryRepositoryTest {

    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        movieRepository = new MovieInMemoryRepository();
    }

    @Test
    @DisplayName("Test finding a not existing movie")
    void shouldNotFindById() {
        Optional<Movie> movie = movieRepository.findById(999);
        assertTrue(movie.isEmpty(), "Not existing Movie should not be found");
    }

    @Test
    @DisplayName("Test if movie exists by title")
    void shouldCheckIfMovieExistsByTitle() {
        Movie movie = new Movie("Minionki", "Nieznany");
        movieRepository.save(movie);

        assertTrue(movieRepository.isExists("Minionki"), "Movie should exist");
        assertFalse(movieRepository.isExists("Rambo"), "Movie should not exist");
    }

    @Test
    @DisplayName("Test saving a movie")
    void shouldSaveMovie() {
        Movie movie = new Movie("Minionki", "Nieznany");
        movieRepository.save(movie);

        Optional<Movie> findMovie = movieRepository.findById(movie.getId());
        assertTrue(findMovie.isPresent());
    }

    @Test
    @DisplayName("Test if newly saved movies are properly added to List")
    void shouldFindAllMovies() {
        Movie movie1 = new Movie("Minionki", "Nieznany");
        movieRepository.save(movie1);

        Movie movie2 = new Movie("Narnia", "Nieznany II");
        movieRepository.save(movie2);

        assertEquals(2, movieRepository.findAll().size());
    }
}