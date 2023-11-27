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
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Currency;

@RequiredArgsConstructor
class AccountCreator {

    private final Clock clock;
    private final AccountRepository accountRepository;

    Account createAccount(BigDecimal firstDeposit, Currency currency) {
        try {
            AmountValidator.shouldBePositiveWithTwoDecimals(firstDeposit);
        } catch (AmountValidationException ex) {
            throw InvalidRequestAmountException.ofInitialDeposit(ex.getMessage());
        }

        Account account = new Account(firstDeposit, currency, LocalDateTime.now(clock));
        AccountEntity accountEntity = accountRepository.save(AccountMapper.toEntity(account));

        return AccountMapper.toModel(accountEntity);
    }

}
