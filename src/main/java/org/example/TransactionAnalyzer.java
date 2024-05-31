package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionAnalyzer {
    private final List<Transaction> transactions;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public TransactionAnalyzer(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public double calculateTotalBalance() {
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    public long countTransactionsByMonth(String monthYear) {
        return transactions.stream()
                .filter(t -> t.getDate().substring(3).equals(monthYear))
                .count();
    }

    public List<Transaction> getTop10Expenses() {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparingDouble(Transaction::getAmount))
                .limit(10)
                .toList();
    }

    public String getTopExpenseCategory() {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(Transaction::getDescription, Collectors.summingDouble(Transaction::getAmount)))
                .entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Не знайдено витрат");
    }

    public void getMinMaxExpenseByPeriod(String startDate, String endDate) {
        List<Transaction> filteredTransactions = transactions.stream()
                .filter(t -> isWithinPeriod(t.getDate(), startDate, endDate))
                .toList();

        Optional<Transaction> minExpense = filteredTransactions.stream()
                .filter(t -> t.getAmount() < 0)
                .max(Comparator.comparingDouble(Transaction::getAmount));

        Optional<Transaction> maxExpense = filteredTransactions.stream()
                .filter(t -> t.getAmount() < 0)
                .min(Comparator.comparingDouble(Transaction::getAmount));

        if (minExpense.isPresent()) {
            System.out.println("Найменша витрата: " + minExpense.get());
        } else {
            System.err.println("Не знайдено транзакцій за вказаний період.");
        }

        if (maxExpense.isPresent()) {
            System.out.println("Найдорожча витрата: " + maxExpense.get());
        } else {
            System.err.println("Не знайдено транзакцій за вказаний період.");
        }
    }

    private boolean isWithinPeriod(String date, String startDate, String endDate) {
        LocalDate transactionDate = LocalDate.parse(date, formatter);
        LocalDate periodStart = LocalDate.parse(startDate, formatter);
        LocalDate periodEnd = LocalDate.parse(endDate, formatter);

        return !transactionDate.isBefore(periodStart) && !transactionDate.isAfter(periodEnd);
    }

    public Map<String, Double> getCategoryExpensesForMonth(String monthYear) {
        return transactions.stream()
                .filter(t -> t.getDate().substring(3).equals(monthYear) && t.getAmount() < 0)
                .collect(Collectors.groupingBy(Transaction::getDescription, Collectors.summingDouble(Transaction::getAmount)));
    }

    public String generateMonthlyExpenseReport(String monthYear) {
        StringBuilder report = new StringBuilder();
        report.append("Сумарні витрати по категоріях за ").append(monthYear).append(":\n");
        Map<String, Double> categoryExpenses = getCategoryExpensesForMonth(monthYear);
        categoryExpenses.forEach((category, amount) -> {
            report.append(String.format("%s: %.2f грн ", category, amount));
            int stars = (int) Math.abs(amount) / 1000;
            report.append("*".repeat(Math.max(0, stars)));
            report.append("\n");
        });

        return report.toString();
    }
}
