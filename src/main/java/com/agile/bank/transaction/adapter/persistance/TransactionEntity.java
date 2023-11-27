package com.agile.bank.transaction.adapter.persistance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Builder
@Data
@AllArgsConstructor
public class TransactionEntity {

    private Long id;
    private final Long sourceAccountId;
    private final Long targetAccountId;
    private final BigDecimal amount;
    private final Currency currency;


}
