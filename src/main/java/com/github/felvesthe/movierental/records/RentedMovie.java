package com.github.felvesthe.movierental.records;

import com.github.felvesthe.movierental.models.Movie;

import java.time.LocalDate;

public record RentedMovie(Movie movie, LocalDate localDate) {}
