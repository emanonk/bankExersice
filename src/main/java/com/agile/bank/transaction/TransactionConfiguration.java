package com.agile.bank.transaction;

import com.agile.bank.account.AccountFacade;
import com.agile.bank.transaction.adapter.persistance.InMemoryTransactionRepositoryAdapter;
import com.agile.bank.transaction.port.TransactionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TransactionConfiguration {


    @Bean
    TransactionRepository transactionRepository() {
        return new InMemoryTransactionRepositoryAdapter();
    }

    @Bean
    TransactionApplier transactionSaver(AccountFacade accountFacade, TransactionRepository transactionRepository) {
        return new TransactionApplier(accountFacade, transactionRepository);
    }

    @Bean
    TransactionGetter transactionGetter(TransactionRepository transactionRepository) {
        return new TransactionGetter(transactionRepository);
    }

    @Bean
    TransactionFacade transactionFacade( TransactionApplier transactionApplier, TransactionGetter transactionGetter) {
        return new TransactionFacadeImpl(
                transactionApplier,
                transactionGetter);
    }
}
