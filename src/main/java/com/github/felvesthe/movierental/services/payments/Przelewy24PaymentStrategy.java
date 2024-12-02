package com.github.felvesthe.movierental.services.payments;

import com.github.felvesthe.movierental.enums.StatusCode;

public class Przelewy24PaymentStrategy implements PaymentStrategy {

    @Override
    public StatusCode processPayment() {
        System.out.println("Przelewy24 nie są jeszcze obsługiwane. Wybierz inną metodę płatności.");
        return StatusCode.NOT_IMPLEMENTED;
    }
}
