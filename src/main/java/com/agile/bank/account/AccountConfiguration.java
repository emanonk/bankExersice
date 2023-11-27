package com.agile.bank.account;

import com.agile.bank.account.adapter.persistance.InMemoryAccountRepositoryAdapter;
import com.agile.bank.account.port.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class AccountConfiguration {

    @Bean
    AccountFacade accountFacade() {
        AccountRepository accountRepository = new InMemoryAccountRepositoryAdapter();
        return new AccountFacadeImpl(
                new AccountCreator(Clock.systemDefaultZone(), accountRepository),
                new AccountGetter(accountRepository),
                new AccountDebtor(accountRepository),
                new AccountCreditor(accountRepository));
    }
}
