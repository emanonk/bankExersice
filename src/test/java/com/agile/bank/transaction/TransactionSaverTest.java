package com.agile.bank.transaction;

import com.agile.bank.transaction.adapter.persistance.TransactionEntity;
import com.agile.bank.transaction.domain.Transaction;
import com.agile.bank.transaction.port.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionSaverTest {

    private static final long TRANSACTION_ID = 1L;
    private static final long SOURCE_ACCOUNT_ID = 1L;
    private static final long TARGET_ACCOUNT_ID = 2L;
    private static final Currency USD = Currency.getInstance("USD");

    private static final BigDecimal TRANSFER_AMOUNT = new BigDecimal("30.00");

    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionSaver transactionSaver;

    @Test
    void shouldReturnExistingTransactionSuccessfully() {
        Transaction transactionToBeSaved = Transaction.builder()
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        Transaction expectedTransaction = Transaction.builder()
                .id(TARGET_ACCOUNT_ID)
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        TransactionEntity transactionEntityToBeSaved = TransactionEntity.builder()
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();



        TransactionEntity transactionEntitySaved = TransactionEntity.builder()
                .id(TARGET_ACCOUNT_ID)
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        when(transactionRepository.save(transactionEntityToBeSaved)).thenReturn(transactionEntitySaved);
        Transaction transaction = transactionSaver.save(transactionToBeSaved);

        assertThat(transaction).isEqualTo(expectedTransaction);
    }
}