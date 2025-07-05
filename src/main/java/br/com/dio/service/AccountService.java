package br.com.dio.service;

import br.com.dio.Utils;
import br.com.dio.exception.AccountNotFoundException;
import br.com.dio.exception.NoFundsEnoughException;
import br.com.dio.repository.AccountRepository;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class AccountService {

    private final static AccountRepository accountRepository = new AccountRepository();

    public static final String ENTER_PIX_KEYS_COMMA_SEPARATED = "Digite as chaves PIX (separadas por vírgula):";
    public static final String ACCOUNT_CREATED_SUCCESSFULLY = "Conta criada com sucesso: ";
    public static final String ENTER_INITIAL_FUNDS = "Digite o saldo inicial:";
    public static final String ENTER_TARGET_PIX_KEY = "Digite a chave PIX de destino:";
    public static final String ENTER_SOURCE_PIX_KEY = "Digite a chave PIX de origem:";
    public static final String TRANSFER_FAILED = "Falha na transferência: ";
    public static final String ENTER_AMOUNT_TO_DEPOSIT = "Digite o valor para depositar:";
    public static final String ENTER_PIX_KEY_FOR_WITHDRAWAL = "Digite a chave PIX para saque:";
    public static final String ENTER_AMOUNT_TO_WITHDRAW = "Digite o valor para sacar:";
    public static final String WITHDRAWAL_FAILED = "Falha no saque: ";
    public static final String ENTER_PIX_KEY_FOR_DEPOSIT = "Digite a chave PIX para depósito:";
    public static final String DEPOSIT_FAILED = "Falha no depósito: ";
    public static final String ENTER_PIX_KEY_FOR_ACCOUNT_HISTORY = "Digite a chave PIX para histórico da conta:";

    public static void createAccount() {
        Scanner scanner = new Scanner(in);
        out.println(ENTER_PIX_KEYS_COMMA_SEPARATED);
        var pix = Arrays.stream(scanner.next().split(",")).toList();
        out.println(ENTER_INITIAL_FUNDS);
        var initialFunds = scanner.nextBigDecimal();
        var account = accountRepository.create(pix, initialFunds);
        out.println(ACCOUNT_CREATED_SUCCESSFULLY + account);
    }

    public static void transferToAccount() {
        Scanner scanner = new Scanner(in);
        out.println(ENTER_SOURCE_PIX_KEY);
        var source = scanner.next();
        out.println(ENTER_TARGET_PIX_KEY);
        var target = scanner.next();
        out.println(ENTER_AMOUNT_TO_DEPOSIT);
        var fundsAmount = scanner.nextBigDecimal();
        try {
            accountRepository.transferMoney(source, target, fundsAmount);
        } catch (Exception e) {
            out.println(TRANSFER_FAILED + e.getMessage());
        }
    }

    public static void deposit() {
        Scanner scanner = new Scanner(in);
        out.println(ENTER_PIX_KEY_FOR_DEPOSIT);
        var pix = scanner.next();
        out.println(ENTER_AMOUNT_TO_DEPOSIT);
        var fundsAmount = scanner.nextBigDecimal();
        try {
            accountRepository.deposite(pix, fundsAmount);
        } catch (Exception e) {
            out.println(DEPOSIT_FAILED + e.getMessage());
        }
    }

    public static void withdraw() {
        Scanner scanner = new Scanner(in);
        out.println(ENTER_PIX_KEY_FOR_WITHDRAWAL);
        var pix = scanner.next();
        out.println(ENTER_AMOUNT_TO_WITHDRAW);
        var fundsAmount = scanner.nextBigDecimal();
        try {
            accountRepository.withdraw(pix, fundsAmount);
        } catch (NoFundsEnoughException | AccountNotFoundException e) {
            out.println(WITHDRAWAL_FAILED + e.getMessage());
        }
    }

    public static void checkHistory() {
        Scanner scanner = new Scanner(in);
        out.println(ENTER_PIX_KEY_FOR_ACCOUNT_HISTORY);
        var pix = scanner.next();
        try {
            var sortedHistory = accountRepository.getHistory(pix);
            sortedHistory.forEach((k, v) -> {
                ZonedDateTime zdt = k.atZone(ZoneId.systemDefault());
                out.println(zdt.format(DateTimeFormatter.ISO_DATE_TIME));
                out.println(v.getFirst().transactionId());
                out.println(v.getFirst().description());
                out.println(Utils.formatCurrency(BigDecimal.valueOf(v.size()).divide(BigDecimal.valueOf(100))));

            });
        } catch (AccountNotFoundException ex) {
            out.println(ex.getMessage());
        }
    }
}
