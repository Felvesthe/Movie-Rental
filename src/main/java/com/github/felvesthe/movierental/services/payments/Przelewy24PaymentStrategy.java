package com.github.felvesthe.movierental.services.payments;

public class Przelewy24PaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment() {
        System.out.println("Przelewy24 nie są jeszcze obsługiwane. Wybierz inną metodę płatności.");
        return false;
    }
}
