package br.com.dio.repository;

import br.com.dio.exception.NoFundsEnoughException;
import br.com.dio.model.AccountWallet;
import br.com.dio.model.Investment;
import br.com.dio.model.InvestmentWallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvestmentRepositoryTest {

    private InvestmentRepository investmentRepository;
    private AccountRepository accountRepository;

    @BeforeEach
    void setup() {
        investmentRepository = new InvestmentRepository();
        accountRepository = new AccountRepository();
    }

    @Test
    void deveCriarInvestimentoComIdIncrementadoETaxaEValoresCorretos() {
        long tax = 10;
        BigDecimal initialFunds = new BigDecimal("100.00");

        Investment investment = investmentRepository.create(tax, initialFunds);

        assertNotNull(investment, "O investimento não deve ser nulo");

    }

    @Test
    void deveCriarInvestmentWalletQuandoContaTemFundosESemCarteiraAnterior() {
        Investment investment = investmentRepository.create(10, new BigDecimal("100.00"));
        AccountWallet account = accountRepository.create(
                List.of("pix1"),
                new BigDecimal("150.00")
        );

        InvestmentWallet wallet = investmentRepository.initInvestment(account, investment.id());

        assertNotNull(wallet, "Carteira de investimento não deve ser nula");
    }

    @Test
    void deveLancarExcecaoSeContaNaoPossuirFundosSuficientes() {
        Investment investment = investmentRepository.create(10, new BigDecimal("200.00"));
        AccountWallet account = accountRepository.create(List.of("pix1"), new BigDecimal("100.00")); // fundos insuficientes

        var exception = assertThrows(NoFundsEnoughException.class, () -> {
            investmentRepository.initInvestment(account, investment.id());
        });

        assertEquals("Fundos insuficientes para a transação.", exception.getMessage());
    }

}