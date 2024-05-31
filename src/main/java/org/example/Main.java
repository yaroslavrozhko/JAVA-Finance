package org.example;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        disableSSLVerification();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Виберіть джерело даних:");
        System.out.println("1. Посилання");
        System.out.println("2. Локальний файл");
        int sourceChoice = scanner.nextInt();
        scanner.nextLine();

        String sourcePath;
        TransactionReader reader;
        if (sourceChoice == 1) {
            System.out.println("Введіть посилання:");
            sourcePath = scanner.nextLine();
            reader = new URLCSVTransactionReader();
        } else if (sourceChoice == 2) {
            System.out.println("Введіть шлях до локального файлу:");
            sourcePath = scanner.nextLine();
            reader = new LocalCSVTransactionReader();
        } else {
            System.out.println("Невірний вибір. Вихід з програми.");
            return;
        }

        String allTransactionsString = reader.readTransactions(sourcePath);
        if (allTransactionsString == null) {
            System.out.println("Помилка при читанні файлу.");
            return;
        }

        TransactionProcessor processor = new TransactionProcessor();
        List<Transaction> processedTransactions = processor.processTransactions(allTransactionsString);
        TransactionAnalyzer analyzer = new TransactionAnalyzer(processedTransactions);

        int choice;
        do {
            System.out.println("Меню:");
            System.out.println("1. Переглянути загальний баланс");
            System.out.println("2. Підрахувати кількість транзакцій за місяць");
            System.out.println("3. Отримати 10 найбільших витрат");
            System.out.println("4. Визначити категорію з найбільшими витратами");
            System.out.println("5. Визначити найбільші і найменші витрати за вказаний період");
            System.out.println("6. Створити текстовий звіт про витрати по категоріях за місяць");
            System.out.println("0. Вийти");
            System.out.print("Виберіть опцію: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Загальний баланс: " + analyzer.calculateTotalBalance());
                    break;
                case 2:
                    System.out.println("Введіть місяць та рік у форматі MM-YYYY:");
                    String monthYear = scanner.nextLine();
                    System.out.println("Кількість транзакцій за " + monthYear + ": " + analyzer.countTransactionsByMonth(monthYear));
                    break;
                case 3:
                    System.out.println("Топ 10 найбільших витрат:");
                    analyzer.getTop10Expenses().forEach(System.out::println);
                    break;
                case 4:
                    System.out.println("Категорія з найбільшими витратами: " + analyzer.getTopExpenseCategory());
                    break;
                case 5:
                    System.out.println("Введіть першу дату у форматі DD-MM-YYYY:");
                    String startDate = scanner.nextLine();
                    System.out.println("Введіть останню дату у форматі DD-MM-YYYY:");
                    String endDate = scanner.nextLine();
                    analyzer.getMinMaxExpenseByPeriod(startDate, endDate);
                    break;
                case 6:
                    System.out.println("Введіть місяць та рік у форматі MM-YYYY:");
                    String month = scanner.nextLine();
                    System.out.println(analyzer.generateMonthlyExpenseReport(month));
                    break;
                case 0:
                    System.out.println("Вихід з програми.");
                    break;
                default:
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        } while (choice != 0);
    }

    private static void disableSSLVerification() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                    }
            };
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
