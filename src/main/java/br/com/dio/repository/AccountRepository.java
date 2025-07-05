package br.com.dio.repository;

import br.com.dio.exception.AccountNotFoundException;
import br.com.dio.exception.PixInUseException;
import br.com.dio.model.AccountWallet;
import br.com.dio.model.MoneyAudit;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static br.com.dio.repository.CommonsRepository.checkFundsForTransaction;

@Repository
public class AccountRepository {

    private List<AccountWallet> accounts = new ArrayList<>();

    public AccountWallet create(final List<String> pix, final BigDecimal initialFunds) {
        if (!accounts.isEmpty()) {
            var pixInUse = accounts.stream().flatMap(account -> account.getPix().stream()).toList();
            for (var p : pix) {
                if (pixInUse.contains(p)) {
                    throw new PixInUseException("O PIX " + pix + " já está em uso.");
                }
            }
        }
        var account = new AccountWallet(initialFunds, pix);
        accounts.add(account);
        return account;
    }

    public void deposite(final String pix, final BigDecimal fundsAmount) {
        var target = findByPix(pix);
        target.addMoney(fundsAmount, "Depósito na conta");
    }

    public BigDecimal withdraw(final String pix, final BigDecimal amount) {
        var source = findByPix(pix);
        checkFundsForTransaction(source, amount);
        source.reduceMoney(amount);
        return amount;
    }

    public void transferMoney(final String sourcePix, final String targetPix, final BigDecimal amount) {
        var source = findByPix(sourcePix);
        checkFundsForTransaction(source, amount);
        var target = findByPix(targetPix);
        var message = "Transferir de" + sourcePix + " para " + targetPix;
        target.addMoney(source.reduceMoney(amount), source.getService(), message);
    }

    public AccountWallet findByPix(final String pix) {
        return accounts.stream()
                .filter(account -> account.getPix().contains(pix))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("Carteira não encontrada para o PIX: " + pix));
    }

    public List<AccountWallet> getAccounts() {
        return accounts;
    }

    public Map<Instant, List<MoneyAudit>> getHistory(String pix) {
        AccountWallet wallet = findByPix(pix);
        List<MoneyAudit> audit = wallet.getFinancialTransactions();

        return audit.stream()
                .collect(Collectors.groupingBy(
                        t -> t.createdAt().truncatedTo(ChronoUnit.SECONDS).toInstant()
                ));
    }
}
