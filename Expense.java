package com.expensetracker;

import java.time.LocalDate;

public class Expense {
    private int id;
    private String category;
    private String description;
    private double amount;
    private LocalDate date;

    public Expense(int id, String category, String description, double amount) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.date = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void displayExpense() {
        System.out.printf(
            "%-5d %-12s %-25s ₹%-10.2f %s%n",
            id, category, description, amount, date
        );
    }
}


