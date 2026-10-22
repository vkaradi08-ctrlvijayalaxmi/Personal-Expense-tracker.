package com.expensetracker;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static ArrayList<Expense> expenses = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static int nextId = 1;

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n===== PERSONAL EXPENSE TRACKER =====");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. Show Total Expenses");
            System.out.println("4. Show Category-wise Expenses");
            System.out.println("5. Delete Expense");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            choice = readInteger();

            switch (choice) {
                case 1:
                    addExpense();
                    break;

                case 2:
                    viewExpenses();
                    break;

                case 3:
                    showTotal();
                    break;

                case 4:
                    showCategoryTotal();
                    break;

                case 5:
                    deleteExpense();
                    break;

                case 6:
                    System.out.println("Thank you for using Expense Tracker!");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter 1 to 6.");
            }
        } while (choice != 6);

        sc.close();
    }

    static void addExpense() {

        System.out.print("Enter category (Food/Travel/Shopping/Other): ");
        String category = readNonEmpty();

        System.out.print("Enter description: ");
        String description = readNonEmpty();

        System.out.print("Enter amount: ₹");
        double amount = readPositiveAmount();

        Expense expense = new Expense(
            nextId, category, description, amount
        );

        expenses.add(expense);
        nextId++;

        System.out.println("Expense added successfully!");
    }

    static void viewExpenses() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        System.out.println("\n---------------- ALL EXPENSES ----------------");
        System.out.printf(
            "%-5s %-12s %-25s %-11s %s%n",
            "ID", "Category", "Description", "Amount", "Date"
        );

        for (Expense expense : expenses) {
            expense.displayExpense();
        }
    }

    static void showTotal() {

        double total = 0;

        for (Expense expense : expenses) {
            total = total + expense.getAmount();
        }

        System.out.printf("Total expenses: ₹%.2f%n", total);
    }

    static void showCategoryTotal() {

        System.out.print("Enter category to search: ");
        String category = readNonEmpty();

        double total = 0;
        boolean found = false;

        for (Expense expense : expenses) {
            if (expense.getCategory().equalsIgnoreCase(category)) {
                total = total + expense.getAmount();
                found = true;
            }
            
        }

        if (found) {
            System.out.printf(
                "Total spending on %s: ₹%.2f%n", category, total
            );
        } else {
            System.out.println("No expenses found in this category.");
        }
    }

    static void deleteExpense() {

        System.out.print("Enter expense ID to delete: ");
        int id = readInteger();

        for (int i = 0; i < expenses.size(); i++) {

            if (expenses.get(i).getId() == id) {
                expenses.remove(i);
                System.out.println("Expense deleted successfully!");
                return;
            }
        }

        System.out.println("Expense ID not found.");
    }

    static int readInteger() {

        while (true) {
            try {
                int value = Integer.parseInt(sc.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid whole number: ");
            }
        }
    }

    static double readPositiveAmount() {

        while (true) {
            try {
                double amount = Double.parseDouble(sc.nextLine().trim());

                if (Double.isFinite(amount) && amount > 0) {
                    return amount;
                }

                System.out.print("Amount must be greater than zero. Try again: ₹");

            } catch (NumberFormatException e) {
                System.out.print("Invalid amount. Enter a number: ₹");
            }
        }
    }

    static String readNonEmpty() {

        while (true) {
            String value = sc.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.print("Input cannot be empty. Try again: ");
        }
    }
}



