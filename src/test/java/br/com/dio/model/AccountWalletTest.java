package br.com.dio.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountWalletTest {

    @Test
    void deveCriarCarteiraComFundosIniciaisEPix() {
        BigDecimal valorInicial = new BigDecimal("250.75");
        List<String> chavesPix = List.of("user1@pix", "user2@pix");

        AccountWallet carteira = new AccountWallet(valorInicial, chavesPix);

        assertEquals(25075, carteira.getFunds());
        assertEquals(chavesPix, carteira.getPix());
    }
}