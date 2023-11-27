package com.agile.bank.transaction.port;


import com.agile.bank.transaction.adapter.persistance.TransactionEntity;
import com.agile.bank.transaction.domain.Transaction;

import java.util.Optional;

public interface TransactionRepository {

    Optional<TransactionEntity> findById(Long id);
    TransactionEntity save(TransactionEntity transactionEntity);

}
