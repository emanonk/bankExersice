package com.agile.bank.transaction;

import com.agile.bank.transaction.adapter.persistance.TransactionEntity;
import com.agile.bank.transaction.adapter.persistance.TransactionMapper;
import com.agile.bank.transaction.domain.Transaction;
import com.agile.bank.transaction.port.TransactionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionSaver {

    private final TransactionRepository transactionRepository;

    public Transaction save(Transaction transaction) {

        TransactionEntity transactionEntity = TransactionMapper.toEntity(transaction);

        TransactionEntity savedTransaction = transactionRepository.save(transactionEntity);

        transaction.setId(savedTransaction.getId());

        return transaction;
    }
}
