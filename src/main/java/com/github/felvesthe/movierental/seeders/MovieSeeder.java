package com.github.felvesthe.movierental.seeders;

import com.github.felvesthe.movierental.models.Movie;
import com.github.felvesthe.movierental.repositories.MovieInMemoryRepository;
import com.github.felvesthe.movierental.repositories.interfaces.MovieRepository;
import com.github.javafaker.Faker;

public class MovieSeeder {

    public static void seed() {
        MovieRepository movieRepository = new MovieInMemoryRepository();

        Faker faker = new Faker();

        int i = 0;
        while (i < 10) {
            String movieTitle = faker.book().title();
            String director = faker.book().author();

            if (movieRepository.isExists(movieTitle)) continue;

            Movie movie = new Movie(movieTitle, director);
            movieRepository.save(movie);
            i++;
        }
    }
}
