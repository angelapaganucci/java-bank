package br.com.dio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void deveFormatarValorMonetarioParaRealBrasileiro() {
        BigDecimal valor = new BigDecimal("1234.56");

        String resultado = Utils.formatCurrency(valor);

        assertEquals("R$ 1.234,56", resultado); // Pode variar conforme o sistema: "R$ 1.234,56" vs "R$ 1.234,56"
    }

}