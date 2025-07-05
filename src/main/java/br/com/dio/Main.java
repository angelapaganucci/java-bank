package br.com.dio;

import br.com.dio.repository.AccountRepository;
import br.com.dio.repository.InvestmentRepository;
import br.com.dio.service.AccountService;
import br.com.dio.service.InvestmentService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

import static java.lang.System.exit;
import static java.lang.System.in;
import static java.lang.System.out;

@SpringBootApplication
public class Main {

    private final static AccountRepository accountRepository = new AccountRepository();
    private final static InvestmentRepository investmentRepository = new InvestmentRepository();

    public static final String PRESSIONE_ENTER_PARA_VOLTAR_AO_MENU = "\nPressione ENTER para voltar ao menu...";

    static Scanner scanner = new Scanner(in);

    public static void main(String[] args) {
        out.println("Bem-vindo ao Banco DIO");
        while (true) {
            out.println("Selecione uma opção:");
            out.println("1. Criar Conta");
            out.println("2. Criar Investimento");
            out.println("3. Fazer um investimento");
            out.println("4. Depositar na Conta");
            out.println("5. Sacar da Conta");
            out.println("6. Transferir Dinheiro");
            out.println("7. Depositar no Investimento");
            out.println("8. Sacar do Investimento");
            out.println("9. Listar Contas");
            out.println("10. Listar Investimentos");
            out.println("11. Listar carteiras de Investimentos");
            out.println("12. Atualizar investimentos");
            out.println("13. Histórico da Conta");
            out.println("14. Sair");

            var option = scanner.nextInt();

            switch (option) {
                case 1 -> {
                    AccountService.createAccount();
                    pauseForEnter();
                }
                case 2 -> {
                    InvestmentService.createInvestment();
                    pauseForEnter();
                }
                case 3 -> {
                    InvestmentService.createInvestmentWallet();
                    pauseForEnter();
                }
                case 4 -> {
                    AccountService.deposit();
                    pauseForEnter();
                }
                case 5 -> {
                    AccountService.withdraw();
                    pauseForEnter();
                }
                case 6 -> {
                    AccountService.transferToAccount();
                    pauseForEnter();
                }
                case 7 -> {
                    InvestmentService.incInvestment();
                    pauseForEnter();
                }
                case 8 -> {
                    InvestmentService.rescueInvestment();
                    pauseForEnter();
                }
                case 9 -> {
                    accountRepository.getAccounts().forEach(out::println);
                    pauseForEnter();
                }
                case 10 -> {
                    investmentRepository.getInvestments().forEach(out::println);
                    pauseForEnter();
                }
                case 11 -> {
                    investmentRepository.getWallets().forEach(out::println);
                    pauseForEnter();
                }
                case 12 -> {
                    investmentRepository.updateAmount();
                    out.println("Investments updated successfully.");
                    pauseForEnter();
                }
                case 13 -> {
                    AccountService.checkHistory();
                    pauseForEnter();
                }
                case 14 -> exit(0);
                default -> {
                    out.println("Invalid option, please try again.");
                    pauseForEnter();
                }
            }
        }
    }

    private static void pauseForEnter() {
        out.println(PRESSIONE_ENTER_PARA_VOLTAR_AO_MENU);
        try {
            System.in.read();
            System.in.skip(System.in.available());
        } catch (Exception ex) {
            ex.getMessage();
        }
    }
}