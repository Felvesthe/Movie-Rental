package com.github.felvesthe.movierental.services;

import com.github.felvesthe.movierental.models.Movie;
import com.github.felvesthe.movierental.models.User;
import com.github.felvesthe.movierental.records.RentedMovie;
import com.github.felvesthe.movierental.repositories.interfaces.MovieRepository;
import com.github.felvesthe.movierental.utils.UserInputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private Scanner scanner;

    @Mock
    private UserInputHandler userInputHandler;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserService userService;

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private PaymentService paymentService;

    private MovieService movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        movieService = new MovieService(scanner, userInputHandler, movieRepository, userService, invoiceService);
    }

    @Test
    @DisplayName("Test successful movie rent for 7 days when payment is successful")
    void shouldRentMovie() {
        User user = mock(User.class);
        Movie movie = mock(Movie.class);

        when(user.getRentedMovies()).thenReturn(new ArrayList<>());
        when(userInputHandler.getInteger(anyString(), anyString(), anyInt(), anyInt())).thenReturn(7);
        when(paymentService.processPayment(any())).thenReturn(true);

        movieService.rent(user, movie, paymentService);
        verify(user).addRentedMovie(any());
    }

    @Test
    @DisplayName("Test failed movie rent when payment is unsuccessful")
    void shouldFailRentProcessWhenPaymentFail() {
        User user = new User("jk90", "jk@gmail.com", "password");
        Movie movie = new Movie("Minionki", "Nieznany");
        PaymentService paymentService = mock(PaymentService.class);

        when(paymentService.processPayment(movie)).thenReturn(false);

        movieService.rent(user, movie, paymentService);

        assertFalse(user.hasMovie(movie));
    }

    @Test
    @DisplayName("Test successful movie rent when payment is processed successfully")
    void shouldPassRentProcessWhenPaymentSuccessful() {
        User user = new User("jk90", "jk@gmail.com", "password");
        Movie movie = new Movie("Minionki", "Nieznany");
        PaymentService paymentService = mock(PaymentService.class);

        when(paymentService.processPayment(movie)).thenReturn(true);

        movieService.rent(user, movie, paymentService);

        assertTrue(user.hasMovie(movie));
    }

    @Test
    @DisplayName("Test successful finding a movie by ID")
    void shouldFindMovieById() {
        Movie movie = mock(Movie.class);

        when(userInputHandler.getInteger(anyString(), anyString(), anyInt(), anyInt())).thenReturn(1);
        when(movieRepository.findById(1)).thenReturn(Optional.of(movie));

        Optional<Movie> findMovie = movieService.findMovieById();

        assertTrue(findMovie.isPresent());
        assertEquals(findMovie.get(), movie);
    }

    @Test
    @DisplayName("Test denying access to movie when movie ID is not found")
    void shouldNotGetAccessToMovieWhenNotFound() {
        User user = mock(User.class);

        when(userInputHandler.getInteger(anyString(), anyString(), anyInt(), anyInt())).thenReturn(-1);

        movieService.getAccessToMovie(user);

        verify(user, never()).hasMovie(any(Movie.class));
    }

    @Test
    @DisplayName("Test denying access when user already has the movie")
    void shouldNotGetAccessWhenUserAlreadyHasMovie() {
        User user = mock(User.class);
        Movie movie = mock(Movie.class);

        when(userInputHandler.getInteger(anyString(), anyString(), anyInt(), anyInt())).thenReturn(1);
        when(movieRepository.findById(1)).thenReturn(Optional.of(movie));
        when(user.hasMovie(movie)).thenReturn(true);

        movieService.getAccessToMovie(user);

        verify(user).hasMovie(movie);
        verifyNoMoreInteractions(user);
    }

    @Test
    @DisplayName("Test returning true when user reaches the maximum rent limit")
    void shouldBeTrueWhenUserHasMaxRentLimit() {
        User user = new User("jk90", "jk@gmail.com", "test");

        for (int i = 0; i < MovieService.MAX_RENT_LIMIT; i++) {
            RentedMovie rentedMovie = mock(RentedMovie.class);
            user.addRentedMovie(rentedMovie);
        }

        assertTrue(movieService.isMaxRentLimitReached(user));
    }

    @Test
    @DisplayName("Test returning false when user has not reached the maximum rent limit")
    void shouldBeFalseWhenUserHasNotMaxRentLimit() {
        User user = new User("jk90", "jk@gmail.com", "test");
        RentedMovie rentedMovie = mock(RentedMovie.class);
        user.addRentedMovie(rentedMovie);

        assertFalse(movieService.isMaxRentLimitReached(user));
    }
}