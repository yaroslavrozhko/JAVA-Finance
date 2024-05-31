package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TransactionProcessor {
    List<Transaction> transactions = new ArrayList<>();

    public List<Transaction> processTransactions(String allTransactionsString) {
        try (BufferedReader br = new BufferedReader(new StringReader(allTransactionsString))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Transaction transaction = new Transaction(values[0], Double.parseDouble(values[1]), values[2]);
                transactions.add(transaction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
