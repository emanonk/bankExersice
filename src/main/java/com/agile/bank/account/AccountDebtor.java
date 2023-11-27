package com.agile.bank.account;

import com.agile.bank.account.adapter.persistance.AccountEntity;
import com.agile.bank.account.adapter.persistance.AccountMapper;
import com.agile.bank.account.domain.Account;
import com.agile.bank.account.exception.InsufficientBalanceException;
import com.agile.bank.account.exception.InvalidRequestAmountException;
import com.agile.bank.account.port.AccountRepository;
import com.agile.bank.common.AmountValidationException;
import com.agile.bank.common.AmountValidator;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
class AccountDebtor {

    private final AccountRepository accountRepository;

    void debit(Account account, BigDecimal debitAmount) throws InsufficientBalanceException, InvalidRequestAmountException {
        try {
            AmountValidator.shouldBePositiveWithTwoDecimals(debitAmount);
        } catch (AmountValidationException ex) {
            throw InvalidRequestAmountException.ofDebit(account.getId(), ex.getMessage());
        }

        BigDecimal unsavedNewAccountBalance = account.getBalance().subtract(debitAmount);

        try {
            AmountValidator.shouldBePositive(unsavedNewAccountBalance);
        } catch (AmountValidationException e) {
            throw InsufficientBalanceException.of(account.getId());
        }

        account.setBalance(unsavedNewAccountBalance);

        AccountEntity accountEntity = AccountMapper.toEntity(account);
        accountRepository.save(accountEntity);
    }
}
