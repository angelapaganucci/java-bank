package br.com.dio.repository;

import br.com.dio.exception.AccountNotFoundException;
import br.com.dio.exception.PixInUseException;
import br.com.dio.model.AccountWallet;
import br.com.dio.model.MoneyAudit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountRepositoryTest {

    private AccountRepository accountRepository;

    @BeforeEach
    void configurar() {
        accountRepository = new AccountRepository();
    }

    @Test
    void deveCriarContaQuandoPixNaoEstiverEmUso() {
        List<String> pix = List.of("pix1", "pix2");
        BigDecimal fundosIniciais = new BigDecimal("100.00");

        AccountWallet conta = accountRepository.create(pix, fundosIniciais);

        assertNotNull(conta);
        assertEquals(pix, conta.getPix());
        assertEquals(10000, conta.getFunds());
    }

    @Test
    void deveLancarExcecaoQuandoPixJaEstiverEmUso() {
        List<String> pix1 = List.of("pix1", "pix2");
        List<String> pix2 = List.of("pix2", "pix3");

        accountRepository.create(pix1, new BigDecimal("50.00"));

        PixInUseException excecao = assertThrows(PixInUseException.class, () -> {
            accountRepository.create(pix2, new BigDecimal("75.00"));
        });

        assertTrue(excecao.getMessage().contains("pix2"));
    }

    @Test
    void deveAdicionarFundosQuandoFizerDeposito() {
        List<String> pix = List.of("pix1");
        var conta = accountRepository.create(pix, new BigDecimal("100.00"));

        accountRepository.deposite("pix1", new BigDecimal("50.00"));

        assertEquals(15000, conta.getFunds(), "Saldo após depósito deve ser 15000 centavos");
    }

    @Test
    void deveDiminuirSaldoAoSacarValorValido() {
        List<String> pix = List.of("pix1");
        var conta = accountRepository.create(pix, new BigDecimal("100.00"));

        BigDecimal valorSaque = new BigDecimal("50.00");
        var valorRetornado = accountRepository.withdraw("pix1", valorSaque);

        assertEquals(5000, conta.getFunds());
        assertEquals(valorSaque, valorRetornado);
    }

    @Test
    void deveLancarExcecaoQuandoNaoHouverFundosSuficientes() {
        List<String> pix = List.of("pix1");
        accountRepository.create(pix, new BigDecimal("100.00"));

        BigDecimal valorSaque = new BigDecimal("150.00");

        assertThrows(br.com.dio.exception.NoFundsEnoughException.class, () -> {
            accountRepository.withdraw("pix1", valorSaque);
        });
    }

    @Test
    void deveTransferirDinheiroEntreContas() {
        var pixOrigem = List.of("pixOrigem");
        var pixDestino = List.of("pixDestino");

        var contaOrigem = accountRepository.create(pixOrigem, new BigDecimal("200.00"));
        var contaDestino = accountRepository.create(pixDestino, new BigDecimal("100.00"));

        BigDecimal valorTransferencia = new BigDecimal("50.00");

        accountRepository.transferMoney("pixOrigem", "pixDestino", valorTransferencia);

        assertEquals(15000, contaOrigem.getFunds());

        assertEquals(15000, contaDestino.getFunds());
    }

    @Test
    void deveLancarExcecaoQuandoSaldoInsuficienteParaTransferencia() {
        var pixOrigem = List.of("pixOrigem");
        var pixDestino = List.of("pixDestino");

        accountRepository.create(pixOrigem, new BigDecimal("100.00"));
        accountRepository.create(pixDestino, new BigDecimal("100.00"));

        BigDecimal valorTransferencia = new BigDecimal("150.00");

        assertThrows(br.com.dio.exception.NoFundsEnoughException.class, () -> {
            accountRepository.transferMoney("pixOrigem", "pixDestino", valorTransferencia);
        });
    }

    @Test
    void deveEncontrarContaPeloPix() {
        var pix = List.of("meuPix123");
        var conta = accountRepository.create(pix, new BigDecimal("100.00"));

        var resultado = accountRepository.findByPix("meuPix123");

        assertEquals(conta, resultado);
    }

    @Test
    void deveLancarExcecaoQuandoPixNaoExistir() {
        assertThrows(AccountNotFoundException.class, () -> {
            accountRepository.findByPix("pixInexistente");
        });
    }

    @Test
    void testarGetAccounts_retornaContasCriadas() {
        var listaPix = List.of("pix1");
        var fundosIniciais = new BigDecimal("100.00");
        var conta = accountRepository.create(listaPix, fundosIniciais);

        List<AccountWallet> contas = accountRepository.getAccounts();

        assertTrue(contas.contains(conta), "A lista de contas deve conter a conta criada");
        assertEquals(1, contas.size(), "Deve haver exatamente 1 conta na lista");
    }

    @Test
    void deveRetornarHistoricoAgrupadoPorInstante() {
        var pixList = List.of("pix1");
        var initialFunds = new BigDecimal("10.00");

        accountRepository.create(pixList, initialFunds);

        Map<Instant, List<MoneyAudit>> history = accountRepository.getHistory("pix1");

        assertFalse(history.isEmpty(), "O histórico não deve ser vazio");

        for (Instant instant : history.keySet()) {
            assertEquals(0, instant.getNano() % 1_000_000_000, "Os nanos devem estar zerados (truncado a segundos)");
        }

        history.values().forEach(list -> {
            assertTrue(list.stream().allMatch(ma -> ma instanceof MoneyAudit), "Todos os itens devem ser do tipo MoneyAudit");
        });
    }
}