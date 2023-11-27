package com.agile.bank.transaction.adapter.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@Builder
@AllArgsConstructor
public class AccountTransferResponseDto {

    @NotNull
    private Long transactionId;
    @NotNull
    private Long sourceAccountId;
    @NotNull
    private Long targetAccountId;
    @Positive
    @NotNull
    private BigDecimal amount;
    @NotNull
    private Currency currency;


}
