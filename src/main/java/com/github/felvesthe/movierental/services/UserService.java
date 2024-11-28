package com.github.felvesthe.movierental.services;

import com.github.felvesthe.movierental.records.RentedMovie;
import com.github.felvesthe.movierental.models.User;
import com.github.felvesthe.movierental.repositories.interfaces.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserService {

    private final Scanner scanner;
    private final UserRepository userRepository;
    private final int MAX_ATTEMPTS = 3;

    public UserService(Scanner scanner, UserRepository userRepository) {
        this.scanner = scanner;
        this.userRepository = userRepository;
    }

    public Optional<User> findUserByEmail() {
        Optional<User> user = Optional.empty();
        int attempts = 0;

        while (user.isEmpty() && attempts < MAX_ATTEMPTS) {
            System.out.print("\nLogin (e-mail): ");
            String login = scanner.nextLine();
            user = userRepository.findByEmail(login.toLowerCase());

            if (user.isEmpty()) {
                attempts++;
                if (attempts < MAX_ATTEMPTS) System.out.println("Niepoprawny login! Spróbuj ponownie.");
            }
        }

        return user;
    }

    public boolean validateUserPassword(User user) {
        int attempts = 0;

        while (user != null && attempts < MAX_ATTEMPTS) {
            System.out.print("Podaj hasło: ");
            String password = scanner.nextLine();

            if (!password.equals(user.getPassword())) {
                attempts++;
                System.out.println("Niepoprawne hasło! Pozostała ilość prób: " + (3 - attempts));
            } else {
                user.login();
                return true;
            }
        }

        return false;
    }

    public User loginUser() {
        Optional<User> user = findUserByEmail();

        if (user.isEmpty()) {
            throw new IllegalArgumentException("Nie znaleziono użytkownika. Logowanie zakończone niepowodzeniem.");
        }

        if (!validateUserPassword(user.get())) {
            throw new IllegalArgumentException("Zbyt wiele nieudanych prób logowania. Spróbuj ponownie później.");
        }

        return user.get();
    }

    public boolean showRentedMovies(User user) {
        List<RentedMovie> rentedMovies = user.getRentedMovies();

        System.out.println("\n----------------------------------\n");
        if (rentedMovies.isEmpty()) {
            System.out.println("Obecnie nie wypożyczasz żadnych filmów.");
            return false;
        } else {
            System.out.println("Twoje wypożyczone filmy " + "(" + rentedMovies.size() + " / " + MovieService.MAX_RENT_LIMIT + "):");

            for (int i = 0; i < rentedMovies.size(); i++) {
                System.out.println("-> " + (i + 1) + ". " + rentedMovies.get(i).movie().getTitle() + " (dostępny do " + rentedMovies.get(i).localDate() + ")");
            }

            return true;
        }
    }
}
