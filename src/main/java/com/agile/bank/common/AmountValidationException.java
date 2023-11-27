package com.agile.bank.common;

public class AmountValidationException extends Exception{

    public AmountValidationException(String message) {
        super(message);
    }

    public static AmountValidationException ofPositiveWithTwoDecimals() {
        return new AmountValidationException("Amount should be positive and with two decimals");
    }

    public static AmountValidationException ofPositive() {
        return new AmountValidationException("Amount should be positive");
    }
}
