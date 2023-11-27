package com.agile.bank.account.port;

import com.agile.bank.account.adapter.persistance.AccountEntity;
import com.agile.bank.account.domain.Account;

import java.util.Optional;

public interface AccountRepository {

    Optional<AccountEntity> findById(Long id);
    AccountEntity save(AccountEntity accountEntity);
}
