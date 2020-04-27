package com.epam.izh.rd.online.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class SimpleBigNumbersService implements BigNumbersService {

    /**
     * Метод делит первое число на второе с заданной точностью
     * Например 1/3 с точностью 2 = 0.33
     * @param range точность
     * @return результат
     */
    @Override
    public BigDecimal getPrecisionNumber(int a, int b, int range) {
        double aD = a;
        double bD = b;
        BigDecimal divResult = new BigDecimal(aD / bD);
        return divResult.setScale(range, RoundingMode.HALF_UP);
    }

    /**
     * Метод находит простое число по номеру
     *
     * @param range номер числа, считая с числа 2
     * @return простое число
     */
    @Override
    public BigInteger getPrimaryNumber(int range) {
        long number = 2;
        int primaryNumbersCount = 0;

        while (true) {
            boolean isComposite = false;
            for (int i = 2; i < number; i++) {
                if (number % i == 0) {
                    isComposite = true;
                    break;
                }
            }
            if (!isComposite) {
                primaryNumbersCount++;
            }
            if (primaryNumbersCount > range) {
                return BigInteger.valueOf(number);
            }
            number++;
        }


    }
}
