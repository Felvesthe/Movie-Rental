package com.github.felvesthe.movierental.models;

import java.time.LocalDate;

public class Invoice {

    private int id;
    private String firstName;
    private String lastName;
    private String postalCode;
    private String product;
    private LocalDate localDate;

    public static int currentId = 1;

    public Invoice(String firstName, String lastName, String postalCode, String product) {
        this.id = currentId++;
        this.firstName = firstName;
        this.lastName = lastName;
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
