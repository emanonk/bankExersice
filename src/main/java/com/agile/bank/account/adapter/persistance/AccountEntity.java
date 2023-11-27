package com.agile.bank.account.adapter.persistance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Builder
@Data
@AllArgsConstructor
public class AccountEntity {

    private Long id;
    private BigDecimal balance;
    private final Currency currency;
    private final LocalDateTime createdAt;
}


