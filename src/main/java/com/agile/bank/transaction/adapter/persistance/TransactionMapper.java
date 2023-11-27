package com.agile.bank.transaction.adapter.persistance;

import com.agile.bank.transaction.domain.Transaction;

public class TransactionMapper {

    public static TransactionEntity toEntity(Transaction transaction) {
        return TransactionEntity
                .builder()
                .id(transaction.getId())
                .sourceAccountId(transaction.getSourceAccountId())
                .targetAccountId(transaction.getTargetAccountId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .build();
    }

    public static Transaction toModel(TransactionEntity transactionEntity) {
        return Transaction
                .builder()
                .id(transactionEntity.getId())
                .sourceAccountId(transactionEntity.getSourceAccountId())
                .targetAccountId(transactionEntity.getTargetAccountId())
                .amount(transactionEntity.getAmount())
                .currency(transactionEntity.getCurrency())
                .build();
    }
}
