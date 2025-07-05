package br.com.dio.service;

import br.com.dio.exception.InvestmentNotFoundException;
import br.com.dio.exception.NoFundsEnoughException;
import br.com.dio.exception.WalletNotFoundException;
import br.com.dio.model.Investment;
import br.com.dio.repository.AccountRepository;
import br.com.dio.repository.InvestmentRepository;

import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class InvestmentService {

    private final static InvestmentRepository investmentRepository = new InvestmentRepository();
    private final static AccountRepository accountRepository = new AccountRepository();

    public static final String ENTER_PIX_KEY_FOR_INVESTMENT_WALLET = "Digite a chave PIX para carteira de investimento:";
    public static final String ENTER_INVESTMENT_ID = "Digite o ID do investimento:";
    public static final String INVESTMENT_WALLET_CREATED_SUCCESSFULLY = "Carteira de investimento criada com sucesso: ";
    public static final String FAILED_TO_CREATE_INVESTMENT_WALLET = "Falha ao criar carteira de investimento: ";
    public static final String ENTER_INITIAL_FUNDS = "Digite o saldo inicial:";
    public static final String ENTER_A_TAX_RATE = "Digite uma taxa:";
    public static final String INVESTMENT_WALLET_UPDATED_SUCCESSFULLY = "Carteira de investimento atualizada com sucesso: ";
    public static final String FAILED_TO_DEPOSIT_TO_INVESTMENT_WALLET = "Falha ao depositar na carteira de investimento: ";
    public static final String WITHDRAWAL_FROM_INVESTMENT_WALLET_SUCCESSFUL = "Saque da carteira de investimento realizado com sucesso: ";
    public static final String FAILED_TO_WITHDRAW_FROM_INVESTMENT_WALLET = "Falha ao sacar da carteira de investimento: ";
    public static final String INVESTMENT_CREATED_SUCCESSFULLY = "Investimento criado com sucesso: ";
    public static final String ENTER_AMOUNT_TO_DEPOSIT = "Digite o valor para depositar:";
    public static final String ENTER_AMOUNT_TO_WITHDRAW = "Digite o valor para sacar:";

    public static void createInvestment() {
        Scanner scanner = new Scanner(in);
        out.println(ENTER_A_TAX_RATE);
        var tax = scanner.nextInt();
        out.println(ENTER_INITIAL_FUNDS);
        var initialFunds = scanner.nextBigDecimal();
        Investment investment = investmentRepository.create(tax, initialFunds);
        out.println(INVESTMENT_CREATED_SUCCESSFULLY + investment);
    }

    public static void createInvestmentWallet() {
        Scanner scanner = new Scanner(in);
        out.println(ENTER_PIX_KEY_FOR_INVESTMENT_WALLET);
        var pix = scanner.next();
        out.println(ENTER_INVESTMENT_ID);
        var id = scanner.nextInt();
        try {
            var account = accountRepository.findByPix(pix);
            var wallet = investmentRepository.initInvestment(account, id);
            out.println(INVESTMENT_WALLET_CREATED_SUCCESSFULLY + wallet);
        } catch (InvestmentNotFoundException e) {
            out.println(FAILED_TO_CREATE_INVESTMENT_WALLET + e.getMessage());
        }
    }

    public static void incInvestment() {
        Scanner scanner = new Scanner(in);
        out.println(ENTER_PIX_KEY_FOR_INVESTMENT_WALLET);
        var pix = scanner.next();
        out.println(ENTER_AMOUNT_TO_DEPOSIT);
        var funds = scanner.nextBigDecimal();
        try {
            var wallet = investmentRepository.deposit(pix, funds);
            out.println(INVESTMENT_WALLET_UPDATED_SUCCESSFULLY + wallet);
        } catch (WalletNotFoundException | InvestmentNotFoundException e) {
            out.println(FAILED_TO_DEPOSIT_TO_INVESTMENT_WALLET + e.getMessage());
        }
    }

    public static void rescueInvestment() {
        Scanner scanner = new Scanner(in);
        out.println(ENTER_PIX_KEY_FOR_INVESTMENT_WALLET);
        var pix = scanner.next();
        out.println(ENTER_AMOUNT_TO_WITHDRAW);
        var funds = scanner.nextBigDecimal();
        try {
            var wallet = investmentRepository.withdraw(pix, funds);
            out.println(WITHDRAWAL_FROM_INVESTMENT_WALLET_SUCCESSFUL + wallet);
        } catch (InvestmentNotFoundException | NoFundsEnoughException e) {
            out.println(FAILED_TO_WITHDRAW_FROM_INVESTMENT_WALLET + e.getMessage());
        }
    }
}
