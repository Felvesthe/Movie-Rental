package com.github.felvesthe.movierental.services.payments;

import com.github.felvesthe.movierental.utils.UserInputHandler;

import java.util.Scanner;
import java.util.concurrent.*;

public class BlikPaymentStrategy implements PaymentStrategy {

    private final Scanner scanner;
    private final UserInputHandler userInputHandler;

    public BlikPaymentStrategy(Scanner scanner, UserInputHandler userInputHandler) {
        this.scanner = scanner;
        this.userInputHandler = userInputHandler;
    }

    @Override
    public boolean processPayment() {
        String blikCode = userInputHandler.getFormattedString("Wprowadź 6-cyfrowy kod BLIK: ", "Błąd: Kod BLIK musi składać się z 6 cyfr!", "\\d{6}");

        System.out.println("Kod BLIK zaakceptowany. Trwa przetwarzanie płatności...");

        boolean isConfirmed = simulateBlikPaymentConfirmation();
        if (isConfirmed) {
            System.out.println("\nPłatność zakończona sukcesem!");
            return true;
        } else {
            System.out.println("\nPłatność została odrzucona.");
            return false;
        }
    }

    public boolean simulateBlikPaymentConfirmation() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> task = () -> {
            System.out.print("Potwierdź transakcję w aplikacji (wpisz 'T' w ciągu 10 sekund): ");
            return scanner.next();
        };

        Future<String> future = executor.submit(task);

        try {
            String confirmation = future.get(10, TimeUnit.SECONDS);
            executor.shutdown();

            return "T".equalsIgnoreCase(confirmation.trim());
        } catch (TimeoutException e) {
            System.out.println("\nCzas na potwierdzenie minął.");
            future.cancel(true);
        } catch (Exception e) {
            System.out.println("\nWystąpił błąd podczas potwierdzania transakcji.");
        } finally {
            executor.shutdownNow();
        }

        return false;
    }
}
