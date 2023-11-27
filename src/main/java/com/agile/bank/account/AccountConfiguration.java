package com.agile.bank.account;

import com.agile.bank.account.adapter.persistance.InMemoryAccountRepositoryAdapter;
import com.agile.bank.account.port.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class AccountConfiguration {

    @Bean
    AccountRepository accountRepository() {
        return new InMemoryAccountRepositoryAdapter();
    }

    @Bean
    AccountCreator accountCreator(AccountRepository accountRepository) {
        return new AccountCreator(Clock.systemDefaultZone(), accountRepository);
    }

    @Bean
    AccountGetter accountGetter(AccountRepository accountRepository) {
        return new AccountGetter(accountRepository);
    }

    @Bean
    AccountDebtor accountDebtor(AccountRepository accountRepository) {
        return new AccountDebtor(accountRepository);
    }

    @Bean
    AccountCreditor accountCreditor(AccountRepository accountRepository) {
        return new AccountCreditor(accountRepository);
    }

    @Bean
    AccountFacade accountFacade(AccountCreator accountCreator, AccountGetter accountGetter, AccountDebtor accountDebtor, AccountCreditor accountCreditor) {
        return new AccountFacadeImpl(
                accountCreator,
                accountGetter,
                accountDebtor,
                accountCreditor);
    }
}
