package com.agile.bank.common;

import java.math.BigDecimal;

public class AmountValidator {

    public static void shouldBePositiveWithTwoDecimals(BigDecimal amount) throws AmountValidationException {
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw AmountValidationException.ofPositiveWithTwoDecimals();
        }
        int scale = amount.scale();
        if(scale != 2) {
            throw AmountValidationException.ofPositiveWithTwoDecimals();
        }

    }

    public static void shouldBePositive(BigDecimal amount) throws AmountValidationException {
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw AmountValidationException.ofPositive();
        }
    }
}
