package com.github.felvesthe.movierental.repositories;

import com.github.felvesthe.movierental.models.User;
import com.github.felvesthe.movierental.repositories.interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserInMemoryRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserInMemoryRepository();
    }

    @Test
    @DisplayName("Test finding a not existing user")
    void shouldNotFindNotExistingUser() {
        Optional<User> user = userRepository.findByEmail("j4n3k953@uniwersytetkaliski.edu.pl");
        assertTrue(user.isEmpty(), "Not existing User should not be found");
    }

    @Test
    @DisplayName("Test saving a user")
    void shouldSaveUser() {
        User user = new User("adamnowak", "adam@uniwersytetkaliski.edu.pl", "test");
        userRepository.save(user);

        Optional<User> findUser = userRepository.findByEmail(user.getEmail());
        assertTrue(findUser.isPresent());
        assertEquals(0, findUser.get().getRentedMovies().size());
    }
}