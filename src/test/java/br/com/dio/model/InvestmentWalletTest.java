package br.com.dio.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InvestmentWalletTest {

    @Test
    void deveCriarCarteiraDeInvestimentoComValoresTransferidosDaConta() {
        var pix = List.of("chave@pix");
        var valorInicial = new BigDecimal("200.00");
        var valorInvestido = new BigDecimal("100.00");

        var conta = new AccountWallet(valorInicial, pix);
        var investimento = new Investment(1, 10, BigDecimal.ZERO);

        var carteiraInvestimento = new InvestmentWallet(investimento, conta, valorInvestido);

        assertEquals(10000, carteiraInvestimento.getFunds()); // 100.00 * 100 = 10.000 centavos
        assertEquals(10000, conta.getFunds());                // Conta deve ter o restante: 200.00 - 100.00 = 100.00
    }

    @Test
    void deveAtualizarFundosDaCarteiraDeInvestimentoComBaseNaPorcentagem() {
        var pix = List.of("pix@teste.com");
        var conta = new AccountWallet(new BigDecimal("100.00"), pix);
        var investimento = new Investment(1, 10, BigDecimal.ZERO);
        var carteira = new InvestmentWallet(investimento, conta, new BigDecimal("100.00")); // 10.000 unidades

        long fundosAntes = carteira.getFunds();

        carteira.updateAmount(10); // Aumenta em 10%

        long esperado = fundosAntes + (fundosAntes * 10 / 100);
        assertEquals(esperado, carteira.getFunds());
    }

}