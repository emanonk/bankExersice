package com.agile.bank.account;

import com.agile.bank.account.domain.Account;
import com.agile.bank.account.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
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

    @Override
    public Long createAccount(BigDecimal firstDeposit, Currency currency) {
        Account account = accountCreator.createAccount(firstDeposit, currency);
        return account.getId();
    }

    @Override
    public Account getAccount(Long accountId) {
        return accountGetter.getAccount(accountId);
    }

    @Override
    public void verifyAccountWithCurrencyExists(Long accountId, Currency currency) {
        Account accountInDb = getAccount(accountId);
        if(! Objects.equals(accountId,accountInDb.getId()) || !Objects.equals(currency, accountInDb.getCurrency())) {
            throw AccountNotFoundException.of(accountId);
        }
    }

    @Override
    public void debitAccount(Long accountId, BigDecimal amount) {
        Account account = getAccount(accountId);
        accountDebtor.debit(account, amount);
    }

    @Override
    public void creditAccount(Long accountId, BigDecimal amount) {
        Account account = getAccount(accountId);
        accountCreditor.credit(account, amount);
    }

    @Override
    public BigDecimal getAccountBalance(Long accountId) throws AccountNotFoundException {
        return getAccount(accountId).getBalance();
    }

}
