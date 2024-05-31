package org.example;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class TransactionAnalyzerTest {

    @Test
    public void testCalculateTotalBalanceTest() {
        // Створення тестових даних
        Transaction transaction1 = new Transaction("01-01-2023", 100.0, "Дохід");
        Transaction transaction2 = new Transaction("01-02-2023", -50.0, "Витрата");
        Transaction transaction3 = new Transaction("01-03-2023", 150.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);

        // Створення екземпляру TransactionAnalyzer з тестовими даними
        TransactionAnalyzer analyzer = new TransactionAnalyzer(transactions);

        // Використання SoftAssertions
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(analyzer.calculateTotalBalance())
                .as("Розрахунок загального балансу неправильний")
                .isEqualTo(200.0);

        softly.assertAll();
    }

    @Test
    public void countTransactionsByMonthTest(){
        Transaction transaction1 = new Transaction("01-01-2023", 100.0, "Дохід");
        Transaction transaction2 = new Transaction("02-01-2023", -50.0, "Витрата");
        Transaction transaction3 = new Transaction("01-02-2023", 150.0, "Дохід");
        Transaction transaction4 = new Transaction("02-02-2023", 150.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3, transaction4);

        // Створення екземпляру TransactionAnalyzer з тестовими даними
        TransactionAnalyzer analyzer = new TransactionAnalyzer(transactions);

        // Використання SoftAssertions
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(analyzer.countTransactionsByMonth("01-2023")).isEqualTo(2);
        softly.assertThat(analyzer.countTransactionsByMonth("02-2023")).isEqualTo(2);

        softly.assertAll();
    }

    @Test
    public void getTopExpenseCategoryTest(){
        Transaction transaction1 = new Transaction("01-01-2023", 100.0, "Дохід");
        Transaction transaction2 = new Transaction("02-01-2023", -50.0, "Витрата");
        Transaction transaction3 = new Transaction("01-02-2023", 150.0, "Дохід");
        Transaction transaction4 = new Transaction("02-02-2023", 150.0, "Дохід");
        Transaction transaction5 = new Transaction("03-02-2023", -100.0, "Продукти");
        Transaction transaction6 = new Transaction("03-02-2023", -150.0, "Ліки");
        Transaction transaction7 = new Transaction("04-02-2023", -250.0, "Ліки");

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3, transaction4, transaction5, transaction6, transaction7);

        // Створення екземпляру TransactionAnalyzer з тестовими даними
        TransactionAnalyzer analyzer = new TransactionAnalyzer(transactions);

        // Використання SoftAssertions
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(analyzer.getTopExpenseCategory()).isEqualTo("Ліки");

        softly.assertAll();
    }
}
