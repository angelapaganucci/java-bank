package br.com.dio.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

public class AccountWallet extends Wallet {

    public static final String INITIAL_DEPOSIT = "Depósito inicial";

    @Getter
    private final List<String> pix;

    public AccountWallet(final BigDecimal amount, List<String> pix) {
        super(BankService.ACCOUNT);
        this.pix = pix;
        addMoney(amount, INITIAL_DEPOSIT);
    }

    public void addMoney(final BigDecimal amount, final String description) {
        var money = generateMoney(amount, description);
        this.money.addAll(money);
    }
}
