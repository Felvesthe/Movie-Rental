package com.github.felvesthe.movierental.services;

import com.github.felvesthe.movierental.models.Movie;
import com.github.felvesthe.movierental.models.User;
import com.github.felvesthe.movierental.records.RentedMovie;
import com.github.felvesthe.movierental.repositories.interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private Scanner scanner;

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(scanner, userRepository);
    }

    @Test
    @DisplayName("Test finding existing user by email address")
    void shouldFindUserByEmailAfterFirstAttempt() {
        String email = "tester@uniwersytetkaliski.edu.pl";
        User user = new User("tester", email, "test");

        when(scanner.nextLine()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> findUser = userService.findUserByEmail();

        assertTrue(findUser.isPresent());
        assertEquals(email, findUser.get().getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Test finding not existing user by email address")
    void shouldFailToFindUserAfterMaxAttempts() {
        when(scanner.nextLine()).thenReturn("t3st3r@uniwersytetkaliski.edu.pl");
        when(userRepository.findByEmail("t3st3r@uniwersytetkaliski.edu.pl")).thenReturn(Optional.empty());

        Optional<User> findUser = userService.findUserByEmail();

        assertTrue(findUser.isEmpty());
        verify(userRepository, times(3)).findByEmail(anyString());
    }

    @Test
    @DisplayName("Test successful user's password verification")
    void shouldValidatePasswordSuccessfully() {
        User user = new User("tester", "tester@uniwersytetkaliski.edu.pl", "test");

        when(scanner.nextLine()).thenReturn("test");

        assertTrue(userService.validateUserPassword(user));
        verify(scanner, times(1)).nextLine();
    }

    @Test
    @DisplayName("Test unsuccessful user's password verification")
    void shouldFailToValidatePasswordAfterMaxAttempts() {
        User user = new User("tester", "tester@uniwersytetkaliski.edu.pl", "test");

        when(scanner.nextLine()).thenReturn("tset");

        assertFalse(userService.validateUserPassword(user));
        verify(scanner, times(3)).nextLine();
    }

    @Test
    @DisplayName("Test successful login")
    void shouldLoginSuccessfully() {
        String email = "tester@uniwersytetkaliski.edu.pl";
        User user = new User("tester", email, "test");

        when(scanner.nextLine()).thenReturn(email, "test");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User loggedUser = userService.loginUser();

        assertNotNull(loggedUser);
        assertEquals(email, loggedUser.getEmail());
    }

    @Test
    @DisplayName("Test throwing an exception when user is not found")
    void shouldThrowExceptionWhenUserNotFound() {
        when(scanner.nextLine()).thenReturn("t3st3r@uniwersytetkaliski.edu.pl");
        when(userRepository.findByEmail("t3st3r@uniwersytetkaliski.edu.pl")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.loginUser());
    }

    @Test
    @DisplayName("Test showing user's rented movies")
    void shouldDisplayRentedMovies() {
        User user = mock(User.class);
        RentedMovie rentedMovie = new RentedMovie(new Movie("Minionki", "Nieznany"), LocalDate.now().plusDays(3));
        when(user.getRentedMovies()).thenReturn(List.of(rentedMovie));

        assertTrue(userService.showRentedMovies(user));
        verify(user, times(1)).getRentedMovies();
    }

    @Test
    @DisplayName("Test showing information about no rented movies when user's list is empty")
    void shouldDisplayWhenNoMoviesRented() {
        User user = mock(User.class);
        when(user.getRentedMovies()).thenReturn(List.of());

        assertFalse(userService.showRentedMovies(user));
        verify(user, times(1)).getRentedMovies();
    }
}