package com.agile.bank.transaction.exception;

public class TransactionNotFoundException extends RuntimeException{

    private static final String TRANSACTION_NOT_FOUND_ERROR_MESSAGE = "Transaction with id: %s not found";
    public TransactionNotFoundException(String message) {
        super(message);
    }

    public static TransactionNotFoundException of(Long accountId) {
        String message = String.format(TRANSACTION_NOT_FOUND_ERROR_MESSAGE, accountId);
        return new TransactionNotFoundException(message);
    }
}
