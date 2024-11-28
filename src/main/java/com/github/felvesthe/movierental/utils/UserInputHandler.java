package com.github.felvesthe.movierental.utils;

import java.util.Scanner;

public class UserInputHandler {

    private final Scanner scanner;

    public UserInputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public int getInteger(String message, String errorMessage, int min, int max) {
        int input;

        while (true) {
            System.out.print(message);

            if (scanner.hasNextInt()) {
                input = scanner.nextInt();

                if (input >= min && input <= max) {
                    return input;
                }
            } else {
                scanner.next();
            }

            System.out.println(errorMessage);
        }
    }

    public int getInteger(String message, String errorMessage, int[] allowedValues) {
        int input;

        while (true) {
            System.out.print(message);

            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                for (int value : allowedValues) {
                    if (input == value) return input;
                }
            } else {
                scanner.next();
            }

            System.out.println(errorMessage);
        }
    }

    public String getString(String message) {
        System.out.print(message);
        return scanner.next().trim();
    }

    public String getFormattedString(String message, String errorMessage, String regex) {
        String input;

        while (true) {
            System.out.print(message);
            input = scanner.next().trim();

            if (input.matches(regex)) {
                return input;
            }

            System.out.println(errorMessage);
        }
    }
}
