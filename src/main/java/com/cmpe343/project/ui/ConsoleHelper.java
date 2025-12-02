package com.cmpe343.project.ui;

import java.sql.Date;
import java.util.Scanner;

public class ConsoleHelper {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";

    private Scanner scanner;

    public ConsoleHelper() {
        this.scanner = new Scanner(System.in);
    }

    public void printTitle(String title) {
        System.out.println(BLUE + "\n========================================");
        System.out.println("   " + title.toUpperCase());
        System.out.println("========================================" + RESET);
    }

    public void printError(String message) {
        System.out.println(RED + "ERROR: " + message + RESET);
    }

    public void printSuccess(String message) {
        System.out.println(GREEN + "SUCCESS: " + message + RESET);
    }

    public void printInfo(String message) {
        System.out.println(CYAN + "INFO: " + message + RESET);
    }

    public void printMenuOption(int number, String description) {
        System.out.println(YELLOW + number + ". " + RESET + description);
    }

    public String readString(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                printError("Invalid number. Please try again.");
            }
        }
    }

    public Date readDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
                return null;
            try {
                return Date.valueOf(input);
            } catch (IllegalArgumentException e) {
                printError("Invalid date format. Use YYYY-MM-DD.");
            }
        }
    }
}
