package com.agile.bank.transaction.adapter.persistance;

import com.agile.bank.transaction.domain.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TransactionMapperTest {

    private static final long TRANSACTION_ID = 1L;
    private static final long SOURCE_ACCOUNT_ID = 1L;
    private static final long TARGET_ACCOUNT_ID = 2L;
    private static final Currency USD = Currency.getInstance("USD");

    private static final BigDecimal TRANSFER_AMOUNT = new BigDecimal("30.00");
    @Test
    void toEntity() {
        Transaction transaction = Transaction.builder()
                .id(TRANSACTION_ID)
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        TransactionEntity expectedTransactionEntity = TransactionEntity.builder()
                .id(TRANSACTION_ID)
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        TransactionEntity actualTransactionEntity = TransactionMapper.toEntity(transaction);

        assertThat(actualTransactionEntity).isEqualTo(expectedTransactionEntity);
    }

    @Test
    void toModel() {
        Transaction expectedTransaction = Transaction.builder()
                .id(TRANSACTION_ID)
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        TransactionEntity transactionEntity = TransactionEntity.builder()
                .id(TRANSACTION_ID)
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        Transaction actualTransaction = TransactionMapper.toModel(transactionEntity);

        assertThat(actualTransaction).isEqualTo(expectedTransaction);
    }
}