package com.agile.bank.transaction;

import com.agile.bank.account.AccountFacade;
import com.agile.bank.common.AmountValidator;
import com.agile.bank.transaction.domain.Transaction;
import com.agile.bank.transaction.exception.TransactionFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TransactionFacadeImpl implements TransactionFacade {

    private final AccountFacade accountFacade;
    private final TransactionSaver transactionSaver;
    private final TransactionGetter transactionGetter;

    public Transaction applyTransfer(Transaction transaction) {

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

        return transactionSaver.save(transaction);
    }

    @Override
    public Transaction getAccount(Long transactionId) {
        return transactionGetter.getTransaction(transactionId);
    }

}
