package com.github.felvesthe.movierental.models;

import java.time.LocalDate;

public class Invoice {

    private final int id;
    private final String firstName;
    private final String lastName;
    private final String postalCode;
    private final String product;
    private final LocalDate localDate;

    public static int currentId = 1;

    public Invoice(String firstName, String lastName, String postalCode, String product) {
        this.id = currentId++;
        this.firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
        this.lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();
        this.postalCode = postalCode;
        this.product = product;
        this.localDate = LocalDate.now();
    }

    @Override
    public String toString() {
        return "\n=============================" +
                "\nFAKTURA #" + id + " (" + localDate + ")" +
                "\nImię: " + firstName +
                "\nNazwisko: " + lastName +
                "\nKod pocztowy: " + postalCode +
                "\n\nProdukt: " + product +
                "\n=============================";
    }
}
