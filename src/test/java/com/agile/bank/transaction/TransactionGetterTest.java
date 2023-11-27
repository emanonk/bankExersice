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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionGetterTest {

    private static final long TRANSACTION_ID = 1L;
    private static final long SOURCE_ACCOUNT_ID = 1L;
    private static final long TARGET_ACCOUNT_ID = 2L;
    private static final Currency USD = Currency.getInstance("USD");

    private static final BigDecimal TRANSFER_AMOUNT = new BigDecimal("30.00");

    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionGetter transactionGetter;

    @Test
    void shouldReturnExistingTransactionSuccessfully() {
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

        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.of(transactionEntity));
        Transaction transaction = transactionGetter.getTransaction(TRANSACTION_ID);

        assertThat(transaction).isEqualTo(expectedTransaction);
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFound() {
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionGetter.getTransaction(TRANSACTION_ID))
                .hasMessage("Transaction with id: 1 not found");
    }
}