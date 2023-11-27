package com.agile.bank.account.adapter.persistance;

import com.agile.bank.account.domain.Account;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {

    private static final long ACCOUNT_ID = 1L;
    private static final Currency USD = Currency.getInstance("USD");
    private static final LocalDateTime ACCOUNT_CREATED_AT = LocalDateTime.of(2023,11,26,11,30);
    private static final BigDecimal ACCOUNT_BALANCE = new BigDecimal("30.00");

    @Test
    void toEntity() {
        AccountEntity expectedAccountEntity = AccountEntity.builder()
                .id(ACCOUNT_ID)
                .balance(ACCOUNT_BALANCE)
                .currency(USD)
                .createdAt(ACCOUNT_CREATED_AT)
                .build();

        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .balance(ACCOUNT_BALANCE)
                .currency(USD)
                .createdAt(ACCOUNT_CREATED_AT)
                .build();

        AccountEntity accountEntity = AccountMapper.toEntity(account);

        assertThat(accountEntity).isEqualTo(expectedAccountEntity);


    }

    @Test
    void toModel() {
        AccountEntity accountEntity = AccountEntity.builder()
                .id(ACCOUNT_ID)
                .balance(ACCOUNT_BALANCE)
                .currency(USD)
                .createdAt(ACCOUNT_CREATED_AT)
                .build();

        Account expectedAccount = Account.builder()
                .id(ACCOUNT_ID)
                .balance(ACCOUNT_BALANCE)
                .currency(USD)
                .createdAt(ACCOUNT_CREATED_AT)
                .build();

        Account account = AccountMapper.toModel(accountEntity);

        assertThat(account).isEqualTo(expectedAccount);
    }
}