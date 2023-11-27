package com.agile.bank.account.adapter.persistance;

import com.agile.bank.account.domain.Account;

public class AccountMapper {

    public static AccountEntity toEntity(Account account) {
        return AccountEntity
                .builder()
                .id(account.getId())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .createdAt(account.getCreatedAt())
                .build();
    }

    public static Account toModel(AccountEntity accountEntity) {
        return Account
                .builder()
                .id(accountEntity.getId())
                .balance(accountEntity.getBalance())
                .currency(accountEntity.getCurrency())
                .createdAt(accountEntity.getCreatedAt())
                .build();
    }
}
