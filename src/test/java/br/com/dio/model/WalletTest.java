package br.com.dio.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Test
    void deveRetornarTodasAsTransacoesFinanceirasDaCarteira() {
        var pix = List.of("pix@teste.com");
        var carteira = new AccountWallet(new BigDecimal("50.00"), pix); // 5.000 unidades
        var transacoes = carteira.getFinancialTransactions();

        int quantidadeEsperada = 5000;

        assertEquals(quantidadeEsperada, transacoes.size());
    }

    @Test
    void deveFormatarToStringCorretamenteComValorMonetario() {
        var pix = List.of("teste@pix.com");
        var carteira = new AccountWallet(new BigDecimal("150.00"), pix);

        var resultado = carteira.toString();

        assertEquals("Wallet{service=ACCOUNT, money= R$ 150,00}", resultado);
    }

}