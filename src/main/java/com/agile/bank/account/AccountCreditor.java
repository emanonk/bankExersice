package com.agile.bank.account;

import com.agile.bank.account.adapter.persistance.AccountEntity;
import com.agile.bank.account.adapter.persistance.AccountMapper;
import com.agile.bank.account.domain.Account;
import com.agile.bank.account.exception.InvalidRequestAmountException;
import com.agile.bank.account.port.AccountRepository;
import com.agile.bank.common.AmountValidationException;
import com.agile.bank.common.AmountValidator;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
class AccountCreditor {

    private final AccountRepository accountRepository;

    void credit(Account account, BigDecimal creditAmount) {
        try {
            AmountValidator.shouldBePositiveWithTwoDecimals(creditAmount);
        } catch (AmountValidationException ex) {
            throw InvalidRequestAmountException.ofCredit(account.getId(), ex.getMessage());
        }
        BigDecimal newAccountBalance = account.getBalance().add(creditAmount);
        account.setBalance(newAccountBalance);

        AccountEntity accountEntity = AccountMapper.toEntity(account);
        accountRepository.save(accountEntity);
    }
}
