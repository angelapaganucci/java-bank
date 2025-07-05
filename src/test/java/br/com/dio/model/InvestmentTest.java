package br.com.dio.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class InvestmentTest {

    @Test
    void deveRetornarRepresentacaoCorretaComoTexto() {
        Investment investimento = new Investment(1, 10, new BigDecimal("150.00"));

        String representacao = investimento.toString();

        assertEquals("Investment{id=1, tax=10%, initialFunds=R$ 150,00}", representacao);
    }

}