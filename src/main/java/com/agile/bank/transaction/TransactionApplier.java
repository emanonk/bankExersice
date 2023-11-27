package com.agile.bank.transaction;

import com.agile.bank.account.AccountFacade;
import com.agile.bank.common.AmountValidator;
import com.agile.bank.transaction.adapter.persistance.TransactionEntity;
import com.agile.bank.transaction.adapter.persistance.TransactionMapper;
import com.agile.bank.transaction.domain.Transaction;
import com.agile.bank.transaction.exception.TransactionFailedException;
import com.agile.bank.transaction.port.TransactionRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class TransactionApplier {

    private final AccountFacade accountFacade;
    private final TransactionRepository transactionRepository;


    public Transaction apply(Transaction transaction) {


        if (transaction.getSourceAccountId().equals(transaction.getTargetAccountId())) {
            throw TransactionFailedException.ofSameAccountTransfer(transaction.getSourceAccountId(), transaction.getTargetAccountId());
        }

        try {
            AmountValidator.shouldBePositiveWithTwoDecimals(transaction.getAmount());
            accountFacade.verifyAccountWithCurrencyExists(transaction.getSourceAccountId(), transaction.getCurrency());
            accountFacade.verifyAccountWithCurrencyExists(transaction.getTargetAccountId(), transaction.getCurrency());
            accountFacade.debitAccount(transaction.getSourceAccountId(), transaction.getAmount());
            accountFacade.creditAccount(transaction.getTargetAccountId(), transaction.getAmount());
        } catch (Exception ex) {
            throw TransactionFailedException.of(transaction.getSourceAccountId(), transaction.getTargetAccountId(), transaction.getCurrency().getCurrencyCode(), ex.getMessage());
        }

        BigDecimal amount = transaction.getAmount();
        amount = amount.setScale(2);
        transaction.setAmount(amount);

        TransactionEntity transactionEntity = TransactionMapper.toEntity(transaction);

        TransactionEntity savedTransaction = transactionRepository.save(transactionEntity);

        transaction.setId(savedTransaction.getId());

        return transaction;
    }
}
