
package com.expensetracker;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ExpenseTrackerGUI extends JFrame {

    private final ArrayList<Expense> expenses = new ArrayList<>();
    private int nextId = 1;

    private final JTextField descriptionField = new JTextField();
    private final JTextField amountField = new JTextField();

    private final JComboBox<String> categoryBox =
            new JComboBox<>(new String[]{
                    "Food", "Travel", "Shopping", "Bills", "Other"
            });

    private final DefaultTableModel tableModel =
            new DefaultTableModel(
                    new String[]{
                            "ID", "Category", "Description",
                            "Amount (₹)", "Date"
                    }, 0) {

                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    private final JTable expenseTable = new JTable(tableModel);
    private final JLabel totalLabel = new JLabel("Total Expenses: ₹0.00");

    public ExpenseTrackerGUI() {

        setTitle("Personal Expense Tracker");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Input form
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 8, 8));

        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryBox);

        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);

        formPanel.add(new JLabel("Amount (₹):"));
        formPanel.add(amountField);

        JButton addButton = new JButton("Add Expense");
        JButton clearButton = new JButton("Clear Fields");

        formPanel.add(addButton);
        formPanel.add(clearButton);

        // Expense table
        expenseTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(expenseTable);

        // Action buttons
        JButton viewButton = new JButton("View All");
        JButton totalButton = new JButton("Show Total");
        JButton categoryTotalButton = new JButton("Category Total");
        JButton deleteButton = new JButton("Delete Selected");
        JButton exitButton = new JButton("Exit");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(viewButton);
        buttonPanel.add(totalButton);
        buttonPanel.add(categoryTotalButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exitButton);

        // Keep table and total together at the bottom
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(totalLabel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e -> addExpense());
        clearButton.addActionListener(e -> clearFields());
        viewButton.addActionListener(e -> refreshTable());
        totalButton.addActionListener(e -> showTotal());
        categoryTotalButton.addActionListener(e -> showCategoryTotal());
        deleteButton.addActionListener(e -> deleteSelectedExpense());
        exitButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void addExpense() {

        String category = (String) categoryBox.getSelectedItem();
        String description = descriptionField.getText().trim();
        String amountText = amountField.getText().trim();

        if (description.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Please enter description and amount.");
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this, "Please enter a valid amount.");
            return;
        }

        if (!Double.isFinite(amount) || amount <= 0) {
            JOptionPane.showMessageDialog(
                    this, "Amount must be greater than zero.");
            return;
        }

        Expense expense = new Expense(
                nextId, category, description, amount);

        expenses.add(expense);
        nextId++;

        refreshTable();
        clearFields();

        JOptionPane.showMessageDialog(
                this, "Expense added successfully!");
    }

    private void refreshTable() {

        tableModel.setRowCount(0);

        for (Expense expense : expenses) {
            tableModel.addRow(new Object[]{
                    expense.getId(),
                    expense.getCategory(),
                    expense.getDescription(),
                    String.format("%.2f", expense.getAmount()),
                    expense.getDate()
            });
        }

        updateTotalLabel();
    }

    private void showTotal() {

        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        JOptionPane.showMessageDialog(
                this,
                String.format("Total expenses: ₹%.2f", total));
    }

    private void showCategoryTotal() {

        String category = (String) JOptionPane.showInputDialog(
                this,
                "Select a category:",
                "Category-wise Expenses",
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{
                        "Food", "Travel", "Shopping", "Bills", "Other"
                },
                "Food");

        if (category == null) {
            return;
        }

        double total = 0;
        int count = 0;

        for (Expense expense : expenses) {
            if (expense.getCategory().equalsIgnoreCase(category)) {
                total += expense.getAmount();
                count++;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                count == 0
                        ? "No expenses found in " + category + "."
                        : String.format(
                                "Total spending on %s: ₹%.2f",
                                category, total));
    }

    private void deleteSelectedExpense() {

        int selectedRow = expenseTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select an expense from the table.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete expense ID " + id + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            expenses.removeIf(expense -> expense.getId() == id);

            refreshTable();

            JOptionPane.showMessageDialog(
                    this, "Expense deleted successfully!");
        }
    }

    private void clearFields() {
        descriptionField.setText("");
        amountField.setText("");
        categoryBox.setSelectedIndex(0);
        descriptionField.requestFocus();
    }

    private void updateTotalLabel() {

        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        totalLabel.setText(
                String.format("Total Expenses: ₹%.2f", total));
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new ExpenseTrackerGUI());
    }
}
