package com.agile.bank.account;

import com.agile.bank.account.adapter.persistance.AccountEntity;
import com.agile.bank.account.adapter.persistance.AccountMapper;
import com.agile.bank.account.domain.Account;
import com.agile.bank.account.exception.AccountNotFoundException;
import com.agile.bank.account.port.AccountRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
class AccountGetter {

    private final AccountRepository accountRepository;

    Account getAccount(Long id) throws AccountNotFoundException {
        Optional<AccountEntity> accountEntity = accountRepository.findById(id);
        return accountEntity.map(AccountMapper::toModel)
                .orElseThrow(() -> AccountNotFoundException.of(id));
    }
}
