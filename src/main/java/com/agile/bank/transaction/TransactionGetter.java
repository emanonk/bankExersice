package com.agile.bank.transaction;

import com.agile.bank.transaction.adapter.persistance.TransactionEntity;
import com.agile.bank.transaction.adapter.persistance.TransactionMapper;
import com.agile.bank.transaction.domain.Transaction;
import com.agile.bank.transaction.exception.TransactionNotFoundException;
import com.agile.bank.transaction.port.TransactionRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class TransactionGetter {

    private final TransactionRepository transactionRepository;

    public Transaction getTransaction(Long id) {
        Optional<TransactionEntity> transactionEntity = transactionRepository.findById(id);
        return transactionEntity.map(TransactionMapper::toModel)
                .orElseThrow(() -> TransactionNotFoundException.of(id));
    }
}
