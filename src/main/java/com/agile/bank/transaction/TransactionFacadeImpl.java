package com.agile.bank.transaction;

import com.agile.bank.transaction.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TransactionFacadeImpl implements TransactionFacade {


    private final TransactionApplier transactionApplier;
    private final TransactionGetter transactionGetter;

    public Transaction applyTransfer(Transaction transaction) {
        return transactionApplier.apply(transaction);
    }

    @Override
    public Transaction getTransaction(Long transactionId) {
        return transactionGetter.getTransaction(transactionId);
    }

}
