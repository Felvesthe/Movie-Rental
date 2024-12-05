package com.github.felvesthe.movierental.services.payments;

import com.github.felvesthe.movierental.enums.StatusCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Przelewy24PaymentStrategyTest {

    private final Przelewy24PaymentStrategy przelewy24PaymentStrategy = new Przelewy24PaymentStrategy();

    @Test
    @DisplayName("Test if przelewy24 payment method returns NOT_IMPLEMENTED status")
    void shouldReturnNotImplementedStatus() {
        assertEquals(StatusCode.NOT_IMPLEMENTED, przelewy24PaymentStrategy.processPayment());
    }
}