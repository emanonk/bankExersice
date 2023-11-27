package com.agile.bank.transaction;

import com.agile.bank.transaction.domain.Transaction;

public interface TransactionFacade {

    Transaction applyTransfer(Transaction transaction);
    Transaction getAccount(Long transactionId);
}
