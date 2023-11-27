package com.agile.bank.account;

import com.agile.bank.account.domain.Account;
import com.agile.bank.account.exception.AccountNotFoundException;
import com.agile.bank.account.exception.InsufficientBalanceException;
import com.agile.bank.account.exception.InvalidRequestAmountException;

import java.math.BigDecimal;
import java.util.Currency;

public interface AccountFacade {

    Long createAccount(BigDecimal firstDeposit, Currency currency) throws InvalidRequestAmountException;

    Account getAccount(Long accountId) throws AccountNotFoundException;

    void verifyAccountWithCurrencyExists(Long accountId, Currency currency);

    void debitAccount(Long accountId, BigDecimal amount);

    void creditAccount(Long accountId, BigDecimal amount);

    BigDecimal getAccountBalance(Long accountId) throws AccountNotFoundException;


}
