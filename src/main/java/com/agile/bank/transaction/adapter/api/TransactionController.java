package com.agile.bank.transaction.adapter.api;


import com.agile.bank.transaction.TransactionFacade;
import com.agile.bank.transaction.adapter.api.dto.AccountTransferRequestDto;
import com.agile.bank.transaction.adapter.api.dto.AccountTransferResponseDto;
import com.agile.bank.transaction.domain.Transaction;
import com.agile.bank.transaction.exception.TransactionFailedException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "bank/v1/transaction", produces = APPLICATION_JSON_VALUE)
@OpenAPIDefinition(info = @Info(title = "Transaction Controller V1"))
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionFacade transactionFacade;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AccountTransferResponseDto> applyTransaction(
            @RequestBody @Valid AccountTransferRequestDto accountTransferRequestDto) {

        Currency currency;
        try {
            currency  = Currency.getInstance(accountTransferRequestDto.getCurrency());
        } catch (IllegalArgumentException ex) {
            throw TransactionFailedException.ofInvalidCurrency(accountTransferRequestDto.getSourceAccountId(), accountTransferRequestDto.getTargetAccountId(), accountTransferRequestDto.getCurrency());
        }

        Transaction transactionRequest = Transaction.builder()
                .sourceAccountId(accountTransferRequestDto.getSourceAccountId())
                .targetAccountId(accountTransferRequestDto.getTargetAccountId())
                .amount(accountTransferRequestDto.getAmount())
                .currency(currency)
                .build();

        Transaction transaction = transactionFacade.applyTransfer(transactionRequest);

        AccountTransferResponseDto accountTransferResponseDto = AccountTransferResponseDto.builder()
                .transactionId(transaction.getId())
                .sourceAccountId(transaction.getSourceAccountId())
                .targetAccountId(transaction.getTargetAccountId())
                .amount(transaction.getAmount().toPlainString())
                .currency(transaction.getCurrency())
                .build();

        return ResponseEntity.ok(accountTransferResponseDto);
    }

    @GetMapping(path = "/{transactionId}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AccountTransferResponseDto> getTransaction(
            @PathVariable("transactionId") Long transactionId) {

        Transaction transaction = transactionFacade.getTransaction(transactionId);

        AccountTransferResponseDto accountTransferResponseDto = AccountTransferResponseDto.builder()
                .transactionId(transaction.getId())
                .sourceAccountId(transaction.getSourceAccountId())
                .targetAccountId(transaction.getTargetAccountId())
                .amount(transaction.getAmount().toPlainString())
                .currency(transaction.getCurrency())
                .build();

        return ResponseEntity.ok(accountTransferResponseDto);
    }

}
