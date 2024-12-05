package com.github.felvesthe.movierental.services.payments;

import com.github.felvesthe.movierental.enums.StatusCode;
import com.github.felvesthe.movierental.utils.UserInputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class BlikPaymentStrategyTest {

    @Mock
    private Scanner scanner;

    @Mock
    private UserInputHandler userInputHandler;

    private BlikPaymentStrategy blikPaymentStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        blikPaymentStrategy = new BlikPaymentStrategy(scanner, userInputHandler);
    }

    @Test
    @DisplayName("Test successful blik payment process")
    void shouldProcessPaymentSuccessfully() {
        when(userInputHandler.getFormattedString(anyString(), anyString(), eq("\\d{6}")))
                .thenReturn("123456");
        when(scanner.next()).thenReturn("T");

        assertEquals(StatusCode.SUCCESS, blikPaymentStrategy.processPayment());
    }

    @Test
    @DisplayName("Test if blik payment timeout fail after 10s waiting")
    void shouldFailPaymentDueToTimeout() {
        when(userInputHandler.getFormattedString(anyString(), anyString(), eq("\\d{6}")))
                .thenReturn("123456");
        when(scanner.next()).thenAnswer(_ -> {
            Thread.sleep(11000);
            return "T";
        });

        assertEquals(StatusCode.ERROR, blikPaymentStrategy.processPayment());
    }
}