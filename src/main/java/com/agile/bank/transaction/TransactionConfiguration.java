package com.agile.bank.transaction;

import com.agile.bank.account.AccountFacade;
import com.agile.bank.transaction.adapter.persistance.InMemoryTransactionRepositoryAdapter;
import com.agile.bank.transaction.port.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TransactionConfiguration {

        @Bean
        TransactionFacade transactionFacade(@Autowired AccountFacade accountFacade) {
            TransactionRepository transactionRepository = new InMemoryTransactionRepositoryAdapter();
            return new TransactionFacadeImpl(
                    accountFacade,
                    new TransactionSaver(transactionRepository),
                    new TransactionGetter(transactionRepository));
        }
}
