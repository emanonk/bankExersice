package com.agile.bank.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AmountValidatorTest {

    @ParameterizedTest(name = "{index} - {0} is valid")
    @ValueSource(strings = {"1.01", "2.99", "1222223.00"})
    void shouldNotThrowExceptionForValidAmount(String number) throws AmountValidationException {
        BigDecimal amount = new BigDecimal(number);
        AmountValidator.shouldBePositiveWithTwoDecimals(amount);
    }

    @ParameterizedTest(name = "{index} - {0} is invalid and an exception is thrown")
    @ValueSource(strings = {"1", "2.0", "3.000", "-4.00", "-5.0", "-6.000"})
    void shouldThrowExceptionForInValidAmount(String number) {
        BigDecimal amount = new BigDecimal(number);
        assertThatThrownBy(() -> {
            AmountValidator.shouldBePositiveWithTwoDecimals(amount);
        });
    }

    @Test
    void shouldNotThrowExceptionForPositiveValue() throws AmountValidationException {
        BigDecimal amount = new BigDecimal("10.00");
        AmountValidator.shouldBePositive(amount);
    }

    @Test
    void shouldThrowExceptionForZeroValue() {
        BigDecimal amount = new BigDecimal("0.00");
        assertThatThrownBy(() -> {
            AmountValidator.shouldBePositive(amount);
        });
    }

    @Test
    void shouldThrowExceptionForNegativeValue() {
        BigDecimal amount = new BigDecimal("-10.00");
        assertThatThrownBy(() -> {
            AmountValidator.shouldBePositive(amount);
        });
    }
}