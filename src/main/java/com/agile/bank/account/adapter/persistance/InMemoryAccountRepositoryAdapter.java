package com.agile.bank.account.adapter.persistance;

import com.agile.bank.account.port.AccountRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryAccountRepositoryAdapter implements AccountRepository {

    private final Map<Long, AccountEntity> accountEntityDb = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);
    @Override
    public Optional<AccountEntity> findById(Long id) {
        AccountEntity accountEntity = accountEntityDb.get(id);
        return Optional.ofNullable(accountEntity);
    }

    @Override
    public AccountEntity save(AccountEntity accountEntity) {

        if(accountEntity.getId() == null) {
            accountEntity.setId(sequence.getAndIncrement());
        }
        accountEntityDb.put(accountEntity.getId(), accountEntity);
        return accountEntity;
    }
}
