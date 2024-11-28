package com.github.felvesthe.movierental.services;

import com.github.felvesthe.movierental.enums.AccessType;
import com.github.felvesthe.movierental.models.Invoice;
import com.github.felvesthe.movierental.models.Movie;
import com.github.felvesthe.movierental.models.User;
import com.github.felvesthe.movierental.records.RentedMovie;
import com.github.felvesthe.movierental.repositories.interfaces.MovieRepository;
import com.github.felvesthe.movierental.utils.UserInputHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MovieService {

    private final Scanner scanner;
    private final UserInputHandler userInputHandler;
    private final MovieRepository movieRepository;
    private final UserService userService;
    private final InvoiceService invoiceService;

    public static final int MAX_RENT_LIMIT = 5;

    public MovieService(Scanner scanner, UserInputHandler userInputHandler, MovieRepository movieRepository, UserService userService, InvoiceService invoiceService) {
        this.scanner = scanner;
        this.userInputHandler = userInputHandler;
        this.movieRepository = movieRepository;
        this.userService = userService;
        this.invoiceService = invoiceService;
    }

    public void getAccessToMovie(User user) {
        Optional<Movie> movie = findMovieById();
        if (movie.isEmpty()) return;
        Movie movieObj = movie.get();

        boolean hasMovie = user.hasMovie(movieObj);
        if (hasMovie) {
            System.out.println("Posiadasz już dostęp do tej produkcji!");
            return;
        }

        PaymentService paymentService = new PaymentService(scanner, userInputHandler, invoiceService);

        showMovieInfo(movieObj);

        AccessType accessType = chooseTypeOfAccess();
        if (accessType == AccessType.RENT) rent(user, movieObj, paymentService);
    }

    public void rent(User user, Movie movie, PaymentService paymentService) {
        if (isMaxRentLimitReached(user)) {
            System.out.println("Posiadasz maksymalną ilość wypożyczonych filmów (" + user.getRentedMovies().size() + " / " + MAX_RENT_LIMIT + "). Zrezygnuj z którejś produkcji lub zaczekaj do jej wygaśnięcia.");
            return;
        }

        int days = userInputHandler.getInteger(
                "Na jaki okres czasu chciał(a)byś wypożyczyć film? (1 / 3 / 7 dni): ",
                "Możesz wypożyczyć film tylko na 1, 3 albo 7 dni. Wybierz którąś z tych wartości.\n",
                new int[]{1, 3, 7}
        );

        boolean paymentProcessStatus = paymentService.processPayment(movie);
        if (!paymentProcessStatus) {
            System.out.println("Wystąpił problem z przetwarzaniem płatności. Spróbuj ponownie później!");
            return;
        }

        LocalDate localDate = LocalDate.now();
        localDate = localDate.plusDays(days);

        RentedMovie rentedMovie = new RentedMovie(movie, localDate);
        user.addRentedMovie(rentedMovie);

        System.out.println(
                "\n----------------------------------\n" +
                "\nFilm o tytule \"" + movie.getTitle() + "\" został przypisany do Twojego konta do dnia " + localDate + "." +
                "\nŻyczymy miłego seansu!"
        );
    }

    public void rentCancellation(User user) {
        boolean hasRentedMovies = userService.showRentedMovies(user);
        if (!hasRentedMovies) return;

        List<RentedMovie> rentedMovies = user.getRentedMovies();

        int movieId = userInputHandler.getInteger(
                "\nZ którego filmu chcesz zrezygnować?" +
                "\nJeśli nie chcesz rezygnować z wypożyczenia, wprowadź wartość \"0\"." +
                "\nWprowadź indeks filmu (1-" + rentedMovies.size() + "): ",
                "Wprowadzono nieprawidłowy indeks filmu, spróbuj ponownie!",
                0, rentedMovies.size());

        if (movieId == 0) {
            System.out.println("Anulowano rezygnację z filmu.");
            return;
        }

        user.removeRentedMovie(movieId - 1);
        System.out.println("Usunięto film z listy wypożyczonych.");
    }

    public Optional<Movie> findMovieById() {
        showAvailableMovies();

        int movieId = userInputHandler.getInteger("\nDo którego filmu chciał(a)byś uzyskać dostęp? Wprowadź ID: ",
                "\nNie znaleziono filmu o podanym ID. Spróbuj ponownie." +
                "\nJeśli chcesz wyjść z wyboru filmu, wprowadź wartość \"0\"." +
                "\nWprowadź ID filmu: ",
                0, movieRepository.findAll().size());

        if (movieId == 0) {
            System.out.println("Anulowano wybór filmu.");
            return Optional.empty();
        }

        Optional<Movie> movie = movieRepository.findById(movieId);

        if (movie.isEmpty()) {
            System.out.println("Nie znaleziono filmu o podanym ID.");
        }

        return movie;
    }

    public void showMovieInfo(Movie movie) {
        System.out.println(
                "\n----------------------------------\n" +
                "\nSzczegóły na temat wybranego przez Ciebie filmu:\n" +
                movie
        );
    }

    public AccessType chooseTypeOfAccess() {
        String type = userInputHandler.getString(
                "\nChcesz wypożyczyć czy kupić film?" +
                        "\nWprowadź polecenie (W/K): "
        ).toLowerCase();

        switch (type) {
            case "w" -> {
                return AccessType.RENT;
            }
            case "k" -> {
                System.err.println("Funkcja kupna filmu nie jest jeszcze dostępna!\n");
                return AccessType.BUY;
            }
            default -> {
                System.out.println("\nWybrano nieprawidłową czynność! Spróbuj ponownie.");
                return chooseTypeOfAccess();
            }
        }
    }

    public void showAvailableMovies() {
        List<Movie> movies = movieRepository.findAll();
        System.out.println(
                "\n----------------------------------\n" +
                "\nLista obecnie dostępnych produkcji:"
        );

        for (int i = 0; i < movies.size(); i++) {
            System.out.println("-> " + (i + 1) + ". " + movies.get(i).getTitle() + " (" + movies.get(i).getDirector() + ")");
        }
    }

    public boolean isMaxRentLimitReached(User user) {
        int rentedMoviesCount = user.getRentedMovies().size();
        return rentedMoviesCount >= MAX_RENT_LIMIT;
    }
}
