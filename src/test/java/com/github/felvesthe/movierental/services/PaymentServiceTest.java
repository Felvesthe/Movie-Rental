package com.github.felvesthe.movierental.services;

import com.github.felvesthe.movierental.enums.StatusCode;
import com.github.felvesthe.movierental.models.Invoice;
import com.github.felvesthe.movierental.models.Movie;
import com.github.felvesthe.movierental.services.payments.BlikPaymentStrategy;
import com.github.felvesthe.movierental.services.payments.PaymentStrategy;
import com.github.felvesthe.movierental.utils.UserInputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private Scanner scanner;

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private UserInputHandler userInputHandler;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentService = new PaymentService(scanner, userInputHandler, invoiceService);
    }

    @Test
    @DisplayName("Test successful payment process")
    void shouldProcessPaymentSuccessfully() {
        Movie movie = new Movie("Minionki", "Nieznany");
        Invoice invoice = new Invoice("Jan", "Kowalski", "62-800", movie.getTitle());

        when(invoiceService.createInvoice(movie)).thenReturn(Optional.of(invoice));
        when(userInputHandler.getInteger(anyString(), anyString(), eq(0), eq(2))).thenReturn(1);

        PaymentStrategy paymentStrategy = mock(PaymentStrategy.class);
        when(paymentStrategy.processPayment()).thenReturn(StatusCode.SUCCESS);

        PaymentService spyPaymentService = spy(paymentService);
        when(spyPaymentService.choosePaymentStrategy()).thenReturn(Optional.of(paymentStrategy));

        assertTrue(spyPaymentService.processPayment(movie));
        verify(invoiceService, times(1)).sendInvoice(invoice);
        verify(paymentStrategy, times(1)).processPayment();
    }

    @Test
    @DisplayName("Test if process fail when payment fail")
    void shouldFailPaymentDueToPaymentFailure() {
        Movie movie = new Movie("Minionki", "Nieznany");
        Invoice invoice = new Invoice("Jan", "Kowalski", "62-800", movie.getTitle());

        when(invoiceService.createInvoice(movie)).thenReturn(Optional.of(invoice));
        when(userInputHandler.getInteger(anyString(), anyString(), eq(0), eq(2))).thenReturn(1);

        PaymentStrategy paymentStrategy = mock(PaymentStrategy.class);
        when(paymentStrategy.processPayment()).thenReturn(StatusCode.ERROR);

        PaymentService spyPaymentService = spy(paymentService);
        when(spyPaymentService.choosePaymentStrategy()).thenReturn(Optional.of(paymentStrategy));

        assertFalse(spyPaymentService.processPayment(movie));
        verify(invoiceService, times(0)).sendInvoice(invoice);
    }

    @Test
    @DisplayName("Test if process fail when invoice is not created properly")
    void shouldFailPaymentDueToInvoiceCreationError() {
        Movie movie = new Movie("Minionki", "Nieznany");
        when(invoiceService.createInvoice(movie)).thenReturn(Optional.empty());

        assertFalse(paymentService.processPayment(movie));
        verify(invoiceService, never()).sendInvoice(any());
    }

    @Test
    @DisplayName("Test if process fail when invalid payment option is selected")
    void shouldFailPaymentDueToInvalidPaymentOption() {
        Movie movie = new Movie("Minionki", "Nieznany");
        Invoice invoice = new Invoice("Jan", "Kowalski", "62-800", movie.getTitle());

        when(invoiceService.createInvoice(movie)).thenReturn(Optional.of(invoice));
        when(userInputHandler.getInteger(anyString(), anyString(), eq(0), eq(2))).thenReturn(0);

        assertFalse(paymentService.processPayment(movie));
        verify(invoiceService, never()).sendInvoice(any());
    }
}