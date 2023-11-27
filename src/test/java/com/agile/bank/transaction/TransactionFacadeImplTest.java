package com.agile.bank.transaction;

import com.agile.bank.transaction.domain.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionFacadeImplTest {

    private static final long TRANSACTION_ID = 1L;
    private static final long SOURCE_ACCOUNT_ID = 1L;
    private static final long TARGET_ACCOUNT_ID = 2L;
    private static final Currency USD = Currency.getInstance("USD");
    private static final BigDecimal TRANSFER_AMOUNT = new BigDecimal("30.00");

    @Mock
    private TransactionApplier transactionApplier;
    @Mock
    private TransactionGetter transactionGetter;
    @InjectMocks
    private TransactionFacadeImpl transactionFacade;
    @Test
    void applyTransfer() {
        Transaction transaction = Transaction.builder()
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        Transaction expcetedTransaction = Transaction.builder()
                .id(TRANSACTION_ID)
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        when(transactionApplier.apply(transaction)).thenReturn(expcetedTransaction);

        Transaction actualTransaction = transactionFacade.applyTransfer(transaction);

        assertThat(actualTransaction).isEqualTo(expcetedTransaction);
    }

    @Test
    void getAccount() {
        Transaction expcetedTransaction = Transaction.builder()
                .id(TRANSACTION_ID)
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        when(transactionGetter.getTransaction(TRANSACTION_ID)).thenReturn(expcetedTransaction);

        Transaction actualTransaction = transactionFacade.getTransaction(TRANSACTION_ID);

        assertThat(actualTransaction).isEqualTo(expcetedTransaction);
    }
}