package com.agile.bank.account.domain;

import com.agile.bank.account.adapter.persistance.AccountEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Builder
@Data
@AllArgsConstructor
public class Account {

    private Long id;
    @EqualsAndHashCode.Exclude
    private BigDecimal balance;
    private final Currency currency;
    private final LocalDateTime createdAt;

    public Account(BigDecimal balance, Currency currency, LocalDateTime createdAt) {
        this.balance = balance;
        this.currency = currency;
        this.createdAt = createdAt;
    }
}
