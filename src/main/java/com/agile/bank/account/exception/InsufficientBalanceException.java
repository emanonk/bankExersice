package com.agile.bank.account.exception;

public class InsufficientBalanceException extends Exception {
    private static final String INSUFFICIENT_BALANCE_ERROR_MESSAGE = "Account with id: %s has insufficient balance for the transfer request";
    public InsufficientBalanceException(String message) {
        super(message);
    }

    public static InsufficientBalanceException of(Long accountId) {
        String message = String.format(INSUFFICIENT_BALANCE_ERROR_MESSAGE, accountId);
        return new InsufficientBalanceException(message);
    }
}
