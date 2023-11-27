package com.agile.bank.transaction.adapter.api;

import com.agile.bank.transaction.TransactionFacade;
import com.agile.bank.transaction.domain.Transaction;
import com.agile.bank.transaction.exception.TransactionFailedException;
import com.agile.bank.transaction.exception.TransactionNotFoundException;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Currency;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    private static final long NON_EXISTING_TRANSACTION_ID = 100L;
    private static final long TRANSACTION_ID = 1L;
    private static final long SOURCE_ACCOUNT_ID = 1L;
    private static final long TARGET_ACCOUNT_ID = 2L;
    private static final Currency USD = Currency.getInstance("EUR");
    private static final BigDecimal TRANSFER_AMOUNT = new BigDecimal("10.00");
    private static final String TRANSACTION_FAILED_ERROR_MESSAGE = "Transaction failed, source account : 1 and target account : 2, with currency: EUR, with error: error-message";
    private static final String TRANSACTION_FAILED_INVALID_CURRENCY = "Transaction failed, source account : 1 and target account : 2, with currency: ERO, with error: Invalid currency";
    private static final String TRANSACTION_NOT_FOUND = "Transaction with id: %s not found";
    @Mock
    private TransactionFacade transactionFacade;
    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setup() {
        ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler();
        RestAssuredMockMvc.standaloneSetup(transactionController, controllerExceptionHandler);
    }

    @Test
    void shouldApplyTransactionSuccessfully() throws IOException {
        Transaction transactionRequest = Transaction.builder()
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        Transaction savedTransaction = Transaction.builder()
                .id(TRANSACTION_ID)
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        when(transactionFacade.applyTransfer(transactionRequest)).thenReturn(savedTransaction);

        given()
                .contentType("application/json")
                .body(createTransactionRequestJson())
                .when()
                .post("/bank/v1/transaction")
                .then()
                .body("transactionId", Matchers.equalTo(1))
                .body("sourceAccountId", Matchers.equalTo(1))
                .body("targetAccountId", Matchers.equalTo(2))
                .body("currency", Matchers.equalTo("EUR"))
                .body("amount", Matchers.equalTo("10.00"))
                .statusCode(200);
    }

    @Test
    void shouldReturnErrorWhenTheTransactionIsFailed() throws IOException {
        Transaction transactionRequest = Transaction.builder()
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        doThrow(TransactionFailedException.of(
                transactionRequest.getSourceAccountId(),
                transactionRequest.getTargetAccountId(),
                transactionRequest.getCurrency().getCurrencyCode(),
                "error-message"))
                .when(transactionFacade).applyTransfer(transactionRequest);

        given()
                .contentType("application/json")
                .body(createTransactionRequestJson())
                .when()
                .post("/bank/v1/transaction")
                .then()
                .body("message", Matchers.equalTo(TRANSACTION_FAILED_ERROR_MESSAGE))
                .statusCode(400);
    }

    @Test
    void shouldReturnErrorForInvalidCurrencyInTheRequest() throws IOException {

        given()
                .contentType("application/json")
                .body(createInvalidTransactionRequestJson())
                .when()
                .post("/bank/v1/transaction")
                .then()
                .body("message", Matchers.equalTo(TRANSACTION_FAILED_INVALID_CURRENCY))
                .statusCode(400);
    }

    @Test
    void shouldReturnTransactionSuccessfully() {
        Transaction savedTransaction = Transaction.builder()
                .id(TRANSACTION_ID)
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        when(transactionFacade.getTransaction(TRANSACTION_ID)).thenReturn(savedTransaction);

        given()
                .contentType("application/json")

                .when()
                .get("/bank/v1/transaction/1")
                .then()
                .body("transactionId", Matchers.equalTo(1))
                .body("sourceAccountId", Matchers.equalTo(1))
                .body("targetAccountId", Matchers.equalTo(2))
                .body("currency", Matchers.equalTo("EUR"))
                .body("amount", Matchers.equalTo("10.00"))
                .statusCode(200);
    }

    @Test
    void shouldReturnTransactionNotFound() {
        doThrow(TransactionNotFoundException.of(NON_EXISTING_TRANSACTION_ID))
                .when(transactionFacade).getTransaction(NON_EXISTING_TRANSACTION_ID);

        String errorMessage = String.format(TRANSACTION_NOT_FOUND, NON_EXISTING_TRANSACTION_ID);
        given()
                .contentType("application/json")

                .when()
                .get("/bank/v1/transaction/" + NON_EXISTING_TRANSACTION_ID)
                .then()
                .body("message", Matchers.equalTo(errorMessage))
                .statusCode(404);
    }

    private String createTransactionRequestJson() throws IOException {
        Path file = Path.of("src/test/resources/test-data/transaction/v1/createTransactionRequest.json");
        return Files.readString(file);
    }

    private String createInvalidTransactionRequestJson() throws IOException {
        Path file = Path.of("src/test/resources/test-data/transaction/v1/createTransactionRequest_invalidCurrency.json");
        return Files.readString(file);
    }
}