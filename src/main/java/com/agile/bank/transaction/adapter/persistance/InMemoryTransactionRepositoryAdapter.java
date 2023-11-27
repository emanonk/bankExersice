package com.agile.bank.transaction.adapter.persistance;

import com.agile.bank.account.adapter.persistance.AccountEntity;
import com.agile.bank.transaction.domain.Transaction;
import com.agile.bank.transaction.port.TransactionRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTransactionRepositoryAdapter implements TransactionRepository {

    private final Map<Long, TransactionEntity> transactionEntityDb = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);
    @Override
    public Optional<TransactionEntity> findById(Long id) {
        TransactionEntity transactionEntity = transactionEntityDb.get(id);
        return Optional.ofNullable(transactionEntity);
    }

    @Override
    public TransactionEntity save(TransactionEntity transactionEntity) {
        if(transactionEntity.getId() == null) {
            transactionEntity.setId(sequence.getAndIncrement());
        }
        transactionEntityDb.put(transactionEntity.getId(), transactionEntity);
        return transactionEntity;
    }


}
