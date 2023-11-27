package com.agile.bank.account.exception;

public class InvalidRequestAmountException extends RuntimeException {

    private static final String INVALID_CREDIT_REQUEST_ERROR_MESSAGE = "Credit request for account with id: %s failed, with error: %s";

    private static final String INVALID_DEBIT_REQUEST_ERROR_MESSAGE = "Debit request for account with id: %s failed, with error: %s";

    private static final String INVALID_INITIAL_DEPOSIT_ERROR_MESSAGE = "New account request failed, with error: %s";
    public InvalidRequestAmountException(String message) {
        super(message);
    }

    public static InvalidRequestAmountException ofCredit(Long accountId, String error) {
        String message = String.format(INVALID_CREDIT_REQUEST_ERROR_MESSAGE, accountId, error);
        return new InvalidRequestAmountException(message);
    }

    public static InvalidRequestAmountException ofDebit(Long accountId, String error) {
        String message = String.format(INVALID_DEBIT_REQUEST_ERROR_MESSAGE, accountId, error);
        return new InvalidRequestAmountException(message);
    }

    public static InvalidRequestAmountException ofInitialDeposit(String error) {
        String message = String.format(INVALID_INITIAL_DEPOSIT_ERROR_MESSAGE, error);
        return new InvalidRequestAmountException(message);
    }
}
