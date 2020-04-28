package com.epam.izh.rd.online.service;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleRegExpService implements RegExpService {

    private String filePath = "src/main/resources/sensitive_data.txt";

    /**
     * Метод должен читать файл sensitive_data.txt (из директории resources) и маскировать в нем конфиденциальную информацию.
     * Номер счета должен содержать только первые 4 и последние 4 цифры (1234 **** **** 5678). Метод должен содержать регулярное
     * выражение для поиска счета.
     *
     * @return обработанный текст
     */
    @Override
    public String maskSensitiveData() {
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            line = reader.readLine();

            Pattern fullAccountNumber = Pattern.compile("\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}");

            Matcher matcher = fullAccountNumber.matcher(line);

            while (matcher.find()) {
                String fullNumber = matcher.group();
                String digitsToHide = fullNumber.substring(5,14);
                String hiddenDigits = fullNumber.replace(digitsToHide, "**** ****");
                line = line.replace(fullNumber, hiddenDigits);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return line;
    }

    /**
     * Метод должен считыввать файл sensitive_data.txt (из директории resources) и заменять плейсхолдер ${payment_amount} и ${balance} на заданные числа. Метод должен
     * содержать регулярное выражение для поиска плейсхолдеров
     *
     * @return обработанный текст
     */
    @Override
    public String replacePlaceholders(double paymentAmount, double balance) {
        String line = null;
        try {

            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            line = reader.readLine();
            Pattern paymentRegExp = Pattern.compile("\\$\\{\\b(\\w*payment_amount)\\}");
            Pattern balanceRegExp = Pattern.compile("\\$\\{\\b(\\w*balance)\\}");
            line = line.replaceAll(paymentRegExp.toString(), String.valueOf((int) paymentAmount));
            line = line.replaceAll(balanceRegExp.toString(), String.valueOf((int) balance));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return line;
    }
}
