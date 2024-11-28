package com.github.felvesthe.movierental.utils;

import com.github.felvesthe.movierental.models.User;
import com.github.felvesthe.movierental.services.MovieService;
import com.github.felvesthe.movierental.services.UserService;

public class MenuHandler {

    private final UserInputHandler userInputHandler;
    private final UserService userService;
    private final MovieService movieService;

    public MenuHandler(UserInputHandler userInputHandler, UserService userService, MovieService movieService) {
        this.userInputHandler = userInputHandler;
        this.userService = userService;
        this.movieService = movieService;
    }

    public void showMenu(User user) {
        boolean isWorking = true;
        boolean showFullMenu = true;

        while (isWorking) {
            System.out.println("\n----------------------------------\n");

            if (showFullMenu) printMenuOptions();

            int option = userInputHandler.getInteger(
                    showFullMenu
                    ? "Wybierz opcję (1-5): "
                    : "Co chcesz teraz zrobić?" +
                    "\nJeśli chcesz wyświetlić Menu, wpisz wartość \"0\"." +
                    "\nWybierz opcję (0-5): ",
                    "Niepoprawna wartość. Wprowadź liczbę od " + (showFullMenu ? "1" : "0") + " do 5",
                    0, 5
            );

            if (option == 0) {
                showFullMenu = true;
                continue;
            }

            switch (option) {
                case 1 -> {
                    userService.showRentedMovies(user);
                    showFullMenu = false;
                }
                case 2 -> {
                    movieService.getAccessToMovie(user);
                    showFullMenu = true;
                }
                case 3 -> {
                    movieService.rentCancellation(user);
                    showFullMenu = true;
                }
                case 4 -> {
                    movieService.showAvailableMovies();
                    showFullMenu = false;
                }
                case 5 -> isWorking = false;
                default -> System.out.println("Wybrano nieprawidłową opcję. Spróbuj ponownie!");
            }
        }

        System.out.println("Dziękujemy za skorzystanie z naszych usług, " + user.getUsername() + "!");
    }

    private void printMenuOptions() {
        System.out.print(
                "Co chcesz zrobić?" +
                        "\n-> 1. Wirtualna półka" +
                        "\n-> 2. Wyszukiwanie filmu" +
                        "\n-> 3. Rezygnacja z wypożyczonego filmu" +
                        "\n-> 4. Wyświetlenie listy dostępnych filmów" +
                        "\n-> 5. Wylogowanie\n"
        );
    }
}
