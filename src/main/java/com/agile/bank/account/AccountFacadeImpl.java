package com.agile.bank.account;

import com.agile.bank.account.domain.Account;
import com.agile.bank.account.exception.AccountNotFoundException;
import com.agile.bank.account.exception.InsufficientBalanceException;
import com.agile.bank.account.exception.InvalidRequestAmountException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountFacadeImpl implements AccountFacade {

    private final AccountCreator accountCreator;
    private final AccountGetter accountGetter;
    private final AccountDebtor accountDebtor;
    private final AccountCreditor accountCreditor;

    //TODO create unit, acceptance, controller, integration tests




    @Override
    public Long createAccount(BigDecimal firstDeposit, Currency currency) throws InvalidRequestAmountException {
        Account account = accountCreator.createAccount(firstDeposit, currency);
        return account.getId();
    }

    @Override
    public Account getAccount(Long accountId) throws AccountNotFoundException {
        return accountGetter.getAccount(accountId);
    }

    @Override
    public void verifyAccountWithCurrencyExists(Long accountId, Currency currency) throws AccountNotFoundException {
        Account accountInDb = getAccount(accountId);
        if(! Objects.equals(accountId,accountInDb.getId()) || Objects.equals(currency, accountInDb.getCurrency())) {
            throw AccountNotFoundException.of(accountId);
        }
    }

    @Override
    public void debitAccount(Long accountId, BigDecimal amount) throws InvalidRequestAmountException, InsufficientBalanceException, AccountNotFoundException {
        Account account = getAccount(accountId);
        accountDebtor.debit(account, amount);
    }

    @Override
    public void creditAccount(Long accountId, BigDecimal amount) throws InvalidRequestAmountException, AccountNotFoundException {
        Account account = getAccount(accountId);
        accountCreditor.credit(account, amount);
    }

    @Override
    public BigDecimal getAccountBalance(Long accountId) throws AccountNotFoundException {
        return getAccount(accountId).getBalance();
    }

}
