package com.agile.bank.transaction.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Builder
@Data
@AllArgsConstructor
public class Transaction {

    private Long id;
    private final Long sourceAccountId;
    private final Long targetAccountId;
    private BigDecimal amount;
    private final Currency currency;

}
