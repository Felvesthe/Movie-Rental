package com.github.felvesthe.movierental;

import com.github.felvesthe.movierental.models.User;
import com.github.felvesthe.movierental.repositories.MovieInMemoryRepository;
import com.github.felvesthe.movierental.repositories.UserInMemoryRepository;
import com.github.felvesthe.movierental.repositories.interfaces.MovieRepository;
import com.github.felvesthe.movierental.repositories.interfaces.UserRepository;
import com.github.felvesthe.movierental.seeders.MovieSeeder;
import com.github.felvesthe.movierental.services.InvoiceService;
import com.github.felvesthe.movierental.services.MovieService;
import com.github.felvesthe.movierental.services.UserService;
import com.github.felvesthe.movierental.utils.MenuHandler;
import com.github.felvesthe.movierental.utils.UserInputHandler;

import java.util.Scanner;

public class App {

    public static void initializeUsers() {
        UserRepository userRepository = new UserInMemoryRepository();

        User user = new User("tester", "tester@uniwersytetkaliski.edu.pl", "test");
        userRepository.save(user);
    }

    public static void initializeMovies() {
        MovieSeeder.seed();
    }

    public static void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            UserInputHandler userInputHandler = new UserInputHandler(scanner);
            UserRepository userRepository = new UserInMemoryRepository();
            MovieRepository movieRepository = new MovieInMemoryRepository();
            UserService userService = new UserService(scanner, userRepository);
            InvoiceService invoiceService = new InvoiceService(userInputHandler);
            MovieService movieService = new MovieService(scanner, userInputHandler, movieRepository, userService, invoiceService);
            MenuHandler menuHandler = new MenuHandler(userInputHandler, userService, movieService);

            User user;

            try {
                user = userService.loginUser();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                return;
            }

            menuHandler.showMenu(user);
        }
    }
}
