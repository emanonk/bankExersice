package com.agile.bank.transaction.adapter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AccountTransferFailedResponseDto {

    private String message;

}
