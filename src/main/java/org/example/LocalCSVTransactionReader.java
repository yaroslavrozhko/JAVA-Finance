package org.example;

import java.io.BufferedReader;
import java.io.FileReader;

public class LocalCSVTransactionReader implements TransactionReader {
    StringBuilder sb = new StringBuilder();
    @Override
    public String readTransactions(String source) {
        try (BufferedReader br = new BufferedReader(new FileReader(source))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }
}
