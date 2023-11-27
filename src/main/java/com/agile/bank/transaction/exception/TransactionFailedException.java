package com.agile.bank.transaction.exception;

public class TransactionFailedException extends RuntimeException {

    private static final String TRANSACTION_FAILED_ERROR_MESSAGE = "Transaction failed, source account : %s and target account : %s, with currency: %s, with error: %s";
    private static final String SAME_ACCOUNT_TRANSFER_ERROR_MESSAGE = "Transaction failed, source account : %s and target account : %s, with error: Transfers are not allowed in the same account";
    private static final String INVALID_CURRENCY = "Invalid currency";


    public TransactionFailedException(String message) {
        super(message);
    }

    public static TransactionFailedException of(Long sourceAccountId, Long targetAccountId, String currency, String errorMessage) {
        String message = String.format(TRANSACTION_FAILED_ERROR_MESSAGE, sourceAccountId, targetAccountId, currency, errorMessage);
        return new TransactionFailedException(message);
    }

    public static TransactionFailedException ofInvalidCurrency(Long sourceAccountId, Long targetAccountId, String currency) {
        String message = String.format(TRANSACTION_FAILED_ERROR_MESSAGE, sourceAccountId, targetAccountId, currency, INVALID_CURRENCY);
        return new TransactionFailedException(message);
    }

    public static TransactionFailedException ofSameAccountTransfer(Long sourceAccountId, Long targetAccountId) {
        String message = String.format(SAME_ACCOUNT_TRANSFER_ERROR_MESSAGE, sourceAccountId, targetAccountId);
        return new TransactionFailedException(message);
    }


}
