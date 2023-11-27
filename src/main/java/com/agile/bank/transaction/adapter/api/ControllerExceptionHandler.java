package com.agile.bank.transaction.adapter.api;

import com.agile.bank.transaction.adapter.api.dto.AccountTransferFailedResponseDto;
import com.agile.bank.transaction.exception.TransactionFailedException;
import com.agile.bank.transaction.exception.TransactionNotFoundException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(responseCode = "400", description = "Transaction Error")
    public AccountTransferFailedResponseDto transactionFailed(TransactionFailedException ex) {
        return new AccountTransferFailedResponseDto(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(responseCode = "400", description = "Transaction Not Found")
    public AccountTransferFailedResponseDto transactionNotFound(TransactionNotFoundException ex) {
        return new AccountTransferFailedResponseDto(ex.getMessage());
    }


}
