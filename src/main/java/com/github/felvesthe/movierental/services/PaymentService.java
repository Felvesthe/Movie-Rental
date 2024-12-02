package com.github.felvesthe.movierental.services;

import com.github.felvesthe.movierental.enums.StatusCode;
import com.github.felvesthe.movierental.models.Invoice;
import com.github.felvesthe.movierental.models.Movie;
import com.github.felvesthe.movierental.services.payments.BlikPaymentStrategy;
import com.github.felvesthe.movierental.services.payments.PaymentStrategy;
import com.github.felvesthe.movierental.services.payments.Przelewy24PaymentStrategy;
import com.github.felvesthe.movierental.utils.UserInputHandler;

import java.util.Optional;
import java.util.Scanner;

public class PaymentService {

    private final Scanner scanner;
    private final UserInputHandler userInputHandler;
    private final InvoiceService invoiceService;

    public PaymentService(Scanner scanner, UserInputHandler userInputHandler, InvoiceService invoiceService) {
        this.scanner = scanner;
        this.userInputHandler = userInputHandler;
        this.invoiceService = invoiceService;
    }

    public boolean processPayment(Movie movie) {
        Optional<Invoice> invoice = invoiceService.createInvoice(movie);

        if (invoice.isEmpty()) {
            System.out.println("Wystąpił problem z danymi wprowadzonymi do faktury. Spróbuj ponownie później!");
            return false;
        }

        StatusCode paymentStatus = null;

        while (paymentStatus == null || paymentStatus == StatusCode.NOT_IMPLEMENTED) {
            Optional<PaymentStrategy> paymentStrategy = choosePaymentStrategy();

            if (paymentStrategy.isEmpty()) {
                System.out.println("Nie wybrano poprawnej metody płatności.");
                return false;
            }

            paymentStatus = paymentStrategy.get().processPayment();
        }

        if (paymentStatus == StatusCode.SUCCESS) {
            invoiceService.sendInvoice(invoice.get());
        }

        return paymentStatus == StatusCode.SUCCESS;
    }

    public Optional<PaymentStrategy> choosePaymentStrategy() {
        int option = userInputHandler.getInteger(
                "\n----------------------------------\n" +
                        "\nDostępne metody płatności:" +
                        "\n-> 1. BLIK" +
                        "\n-> 2. Przelewy24 (niedostępny)" +
                        "\n\nJeśli chcesz anulować płatność, wybierz wartość \"0\"" +
                        "\n\nKtórą metodę płatności wybierasz (nr): ",
                "Niepoprawna metoda płatności. Spróbuj ponownie!",
                0, 2
        );

        if (option == 0) return Optional.empty();

        switch (option) {
            case 1 -> {
                return Optional.of(new BlikPaymentStrategy(scanner, userInputHandler));
            }
            case 2 -> {
                return Optional.of(new Przelewy24PaymentStrategy());
            }
            default -> {
                System.out.println("Niepoprawna metoda płatności. Spróbuj ponownie później!");
                return Optional.empty();
            }
        }
    }
}
