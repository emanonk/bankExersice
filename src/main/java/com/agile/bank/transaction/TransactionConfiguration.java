package com.agile.bank.transaction;

import com.agile.bank.account.AccountFacade;
import com.agile.bank.account.adapter.persistance.InMemoryAccountRepositoryAdapter;
import com.agile.bank.account.port.AccountRepository;
import com.agile.bank.transaction.adapter.persistance.InMemoryTransactionRepositoryAdapter;
import com.agile.bank.transaction.port.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TransactionConfiguration {


    @Bean
    TransactionRepository transactionRepository() {
        return new InMemoryTransactionRepositoryAdapter();
    }

    @Bean
    TransactionSaver transactionSaver(TransactionRepository transactionRepository) {
        return new TransactionSaver(transactionRepository);
    }

    @Bean
    TransactionGetter transactionGetter(TransactionRepository transactionRepository) {
        return new TransactionGetter(transactionRepository);
    }

    @Bean
    TransactionFacade transactionFacade(AccountFacade accountFacade, TransactionSaver transactionSaver, TransactionGetter transactionGetter) {
        TransactionRepository transactionRepository = new InMemoryTransactionRepositoryAdapter();
        return new TransactionFacadeImpl(
                accountFacade,
                transactionSaver,
                transactionGetter);
    }
}
