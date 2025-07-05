package br.com.dio.repository;

import br.com.dio.exception.AccountWithInvestmentException;
import br.com.dio.exception.InvestmentNotFoundException;
import br.com.dio.exception.WalletNotFoundException;
import br.com.dio.model.AccountWallet;
import br.com.dio.model.Investment;
import br.com.dio.model.InvestmentWallet;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static br.com.dio.repository.CommonsRepository.checkFundsForTransaction;

@Repository
public class InvestmentRepository {

    private long nextId = 0;
    @Getter
    private final List<Investment> investments = new ArrayList<>();
    @Getter
    private final List<InvestmentWallet> wallets = new ArrayList<>();

    public Investment create(final long tax, final BigDecimal initialFunds) {
        this.nextId++;
        var investment = new Investment(this.nextId, tax, initialFunds);
        investments.add(investment);
        return investment;
    }

    public InvestmentWallet initInvestment(final AccountWallet account, final long id) {
        if (!wallets.isEmpty()) {
            var accountInUse = wallets.stream().map(InvestmentWallet::getAccount).toList();

            if (accountInUse.contains(account)) {
                throw new AccountWithInvestmentException("A conta já possui uma carteira de investimento.");
            }
        }

        var investment = findById(id);
        checkFundsForTransaction(account, investment.initialFunds());
        var wallet = new InvestmentWallet(investment, account, investment.initialFunds());
        wallets.add(wallet);
        return wallet;
    }

    public InvestmentWallet deposit(final String pix, final BigDecimal funds) {
        var wallet = findWalletByAccountPix(pix);
        wallet.addMoney(wallet.getAccount().reduceMoney(funds), wallet.getService(), "Depósito na carteira de investimento");
        return wallet;
    }

    public InvestmentWallet withdraw(final String pix, final BigDecimal funds) {
        var wallet = findWalletByAccountPix(pix);
        checkFundsForTransaction(wallet, funds);
        wallet.getAccount().addMoney(wallet.reduceMoney(funds), wallet.getService(), "Saque da carteira de investimento");

        if (wallet.getFunds() == 0) {
            wallets.remove(wallet);

        }
        return wallet;
    }

    public void updateAmount() {
        wallets.forEach(w -> w.updateAmount(w.getInvestment().tax()));
    }

    public Investment findById(final long id) {
        return investments.stream()
                .filter(investment -> investment.id() == id)
                .findFirst()
                .orElseThrow(() -> new InvestmentNotFoundException("Não existe investimento para o ID: " + id));
    }

    public InvestmentWallet findWalletByAccountPix(final String pix) {
        return wallets.stream()
                .filter(wallet -> wallet.getAccount().getPix().contains(pix))
                .findFirst()
                .orElseThrow(() -> new WalletNotFoundException("Carteira não encontrada para a conta: " + pix));
    }

}
