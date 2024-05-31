package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class URLCSVTransactionReader implements TransactionReader {
    StringBuilder sb = new StringBuilder();

    @Override
    public String readTransactions(String source) {
        try {
            URL url = new URL(source);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

}