package com.github.felvesthe.movierental.services;

import com.github.felvesthe.movierental.models.Invoice;
import com.github.felvesthe.movierental.models.Movie;
import com.github.felvesthe.movierental.utils.UserInputHandler;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class InvoiceService {

    private final UserInputHandler userInputHandler;

    public InvoiceService(UserInputHandler userInputHandler) {
        this.userInputHandler = userInputHandler;
    }

    public Optional<Invoice> createInvoice(Movie movie) {
        System.out.println("\n----------------------------------\n");
        System.out.println("DANE DO FAKTURY");

        String firstName = "";
        String lastName = "";

        boolean isFirstNameValid = false;
        boolean isLastNameValid = false;

        while (!isFirstNameValid) {
            firstName = userInputHandler.getString("Imię: ");
            isFirstNameValid = InvoiceValidator.getInstance().validateLength("Imię", firstName, 3);
        }

        while (!isLastNameValid) {
            lastName = userInputHandler.getString("Nazwisko: ");
            isLastNameValid = InvoiceValidator.getInstance().validateLength("Nazwisko", lastName, 3);
        }

        String postalCode = userInputHandler.getFormattedString(
                "Kod pocztowy (format 00-000): ",
                "Kod pocztowy musi być w formacie 00-000. Spróbuj ponownie.",
                "\\d{2}-\\d{3}"
        );

        return Optional.of(new Invoice(firstName, lastName, postalCode, movie.getTitle()));
    }

    public void sendInvoice(Invoice invoice) {
        try {
            System.out.println("\n----------------------------------\n");
            System.out.println("Rozpoczynam generowanie i wysyłkę faktury...");
            TimeUnit.SECONDS.sleep(3);
            System.out.println(
                    "Faktura została wysłana na adres e-mail." +
                            "\nPoniżej znajduje się podgląd faktury:" +
                            "\n" + invoice
            );
        } catch (InterruptedException e) {
            System.out.println("Wystąpił problem podczas wysyłania faktury!");
        }
    }

    public static final class InvoiceValidator {
        private static InvoiceValidator instance;

        private InvoiceValidator() {}

        public static InvoiceValidator getInstance() {
            if (instance == null) instance = new InvoiceValidator();
            return instance;
        }

        public boolean validateLength(String fieldName, String text, int minLength) {
            if (text.length() < minLength) {
                System.out.println(
                        "Wartość pola " + fieldName + " jest zbyt krótka! (min: " + minLength + " znaków)" +
                        "\nSpróbuj ponownie!"
                );
                return false;
            }

            return true;
        }
    }
}
