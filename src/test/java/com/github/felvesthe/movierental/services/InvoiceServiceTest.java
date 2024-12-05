package com.github.felvesthe.movierental.services;

import com.github.felvesthe.movierental.models.Invoice;
import com.github.felvesthe.movierental.models.Movie;
import com.github.felvesthe.movierental.utils.UserInputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class InvoiceServiceTest {

    @Mock
    private UserInputHandler userInputHandler;

    private InvoiceService invoiceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        invoiceService = new InvoiceService(userInputHandler);
    }

    @Test
    @DisplayName("Test if invoice is properly validated when all data are OK")
    void shouldCreateInvoiceSuccessfully() {
        Movie movie = new Movie("Minionki", "Nieznany");
        String firstName = "Jan";
        String lastName = "Kowalski";
        String postalCode = "62-800";

        when(userInputHandler.getString("Imię: ")).thenReturn(firstName);
        when(userInputHandler.getString("Nazwisko: ")).thenReturn(lastName);
        when(userInputHandler.getFormattedString(anyString(), anyString(), eq("\\d{2}-\\d{3}")))
                .thenReturn(postalCode);

        InvoiceService.InvoiceValidator validator = InvoiceService.InvoiceValidator.getInstance();
        assertTrue(validator.validateLength("Imię", firstName, 3));
        assertTrue(validator.validateLength("Nazwisko", lastName, 3));

        Optional<Invoice> invoice = invoiceService.createInvoice(movie);
        assertTrue(invoice.isPresent());
    }

    @Test
    @DisplayName("Test if invoice is properly validated when some data are wrong")
    void shouldFailToCreateInvoiceWhenInvalidData() {
        when(userInputHandler.getString("Imię: ")).thenReturn("Ab");
        when(userInputHandler.getString("Nazwisko: ")).thenReturn("C");
        when(userInputHandler.getFormattedString(anyString(), anyString(), eq("\\d{2}-\\d{3}")))
                .thenReturn("12345");

        InvoiceService.InvoiceValidator validator = InvoiceService.InvoiceValidator.getInstance();

        assertFalse(validator.validateLength("Imię", "Ab", 3));
        assertFalse(validator.validateLength("Nazwisko", "C", 3));
    }

    @Test
    @DisplayName("Test if invoice is properly sent when all data are OK")
    void shouldSendInvoiceSuccessfully() {
        Invoice invoice = new Invoice("Jan", "Kowalski", "62-800", "Minionki");
        assertDoesNotThrow(() -> invoiceService.sendInvoice(invoice), "Invoice send successfully");
    }
}