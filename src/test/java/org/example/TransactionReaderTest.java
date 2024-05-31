package org.example;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TransactionReaderTest {

    private Path tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        // Створення тимчасового CSV-файлу
        tempFile = Files.createTempFile("transactions", ".csv");
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write("05-12-2023,-7850,Сільпо\n");
            writer.write("07-12-2023,-1200,Аптека\n");
            writer.write("10-12-2023,80000,Зарплата\n");
            writer.write("12-12-2023,1500,Авторські винагороди\n");
        }
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Видалення тимчасового файлу
        Files.delete(tempFile);
    }

    @Test
    public void testReadTransactions() {
        // Читання даних із тимчасового CSV-файлу
        LocalCSVTransactionReader reader = new LocalCSVTransactionReader();
        String transactionsString = reader.readTransactions(tempFile.toString());

        // Використання SoftAssertions
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(transactionsString).isNotNull();
        softly.assertThat(transactionsString).isEqualTo("05-12-2023,-7850,Сільпо\n07-12-2023,-1200,Аптека\n10-12-2023,80000,Зарплата\n12-12-2023,1500,Авторські винагороди\n");

        softly.assertAll();
    }

    @Test
    public void testProcessTransactions() {
        // Читання даних із тимчасового CSV-файлу
        LocalCSVTransactionReader reader = new LocalCSVTransactionReader();
        String transactionsString = reader.readTransactions(tempFile.toString());

        // Обробка зчитаних даних
        TransactionProcessor processor = new TransactionProcessor();
        List<Transaction> transactions = processor.processTransactions(transactionsString);

        // Використання SoftAssertions
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(transactions).isNotNull();
        softly.assertThat(transactions).hasSize(4);

        softly.assertAll();
    }

    @Test
    public void testIndividualTransaction() {
        // Читання даних із тимчасового CSV-файлу
        LocalCSVTransactionReader reader = new LocalCSVTransactionReader();
        String transactionsString = reader.readTransactions(tempFile.toString());

        // Обробка зчитаних даних
        TransactionProcessor processor = new TransactionProcessor();
        List<Transaction> transactions = processor.processTransactions(transactionsString);

        // Використання SoftAssertions
        SoftAssertions softly = new SoftAssertions();

        Transaction t1 = transactions.get(0);
        softly.assertThat(t1.getDate()).isEqualTo("05-12-2023");
        softly.assertThat(t1.getAmount()).isEqualTo(-7850);
        softly.assertThat(t1.getDescription()).isEqualTo("Сільпо");

        Transaction t2 = transactions.get(1);
        softly.assertThat(t2.getDate()).isEqualTo("07-12-2023");
        softly.assertThat(t2.getAmount()).isEqualTo(-1200);
        softly.assertThat(t2.getDescription()).isEqualTo("Аптека");

        Transaction t3 = transactions.get(2);
        softly.assertThat(t3.getDate()).isEqualTo("10-12-2023");
        softly.assertThat(t3.getAmount()).isEqualTo(80000);
        softly.assertThat(t3.getDescription()).isEqualTo("Зарплата");

        Transaction t4 = transactions.get(3);
        softly.assertThat(t4.getDate()).isEqualTo("12-12-2023");
        softly.assertThat(t4.getAmount()).isEqualTo(1500);
        softly.assertThat(t4.getDescription()).isEqualTo("Авторські винагороди");

        softly.assertAll();
    }
}
