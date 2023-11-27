package com.agile.bank.account.exception;

public class InvalidDebitRequestAmountException extends Exception {

    private static final String INVALID_DEBIT_REQUEST_ERROR_MESSAGE = "Debit request for account with id: %s failed, amount should be positive with maximum two decimals";
    public InvalidDebitRequestAmountException(String message) {
        super(message);
    }

    public static InvalidDebitRequestAmountException of(Long accountId) {
        String message = String.format(INVALID_DEBIT_REQUEST_ERROR_MESSAGE, accountId);
        return new InvalidDebitRequestAmountException(message);
    }
}
