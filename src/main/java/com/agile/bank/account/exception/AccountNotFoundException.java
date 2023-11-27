package com.agile.bank.account.exception;

public class AccountNotFoundException extends Exception {

    private static final String ACCOUNT_NOT_FOUND_ERROR_MESSAGE = "Account with id: %s not found";
    public AccountNotFoundException(String message) {
        super(message);
    }

    public static AccountNotFoundException of(Long accountId) {
        String message = String.format(ACCOUNT_NOT_FOUND_ERROR_MESSAGE, accountId);
        return new AccountNotFoundException(message);
    }
}
