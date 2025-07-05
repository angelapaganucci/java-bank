package br.com.dio.model;


import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Stream;

@ToString
@Getter
public class InvestmentWallet extends Wallet {

    public static final String INITIAL_DEPOSIT_FROM_ACCOUNT = "Depósito inicial da conta";
    public static final String INVESTMENT_UPDATE = "Atualização de investimento";

    private final Investment investment;
    private final AccountWallet account;

    public InvestmentWallet(final Investment investment, final AccountWallet account, final BigDecimal amount) {
        super(BankService.INVESTMENT);
        this.investment = investment;
        this.account = account;
        addMoney(account.reduceMoney(amount), getService(), INITIAL_DEPOSIT_FROM_ACCOUNT);
    }

    public void updateAmount(final long percent) {
        var amount = getFunds() * percent / 100;
        var history = new MoneyAudit(UUID.randomUUID(), getService(), INVESTMENT_UPDATE, OffsetDateTime.now());
        var money = Stream.generate(() -> new Money(history)).limit(amount).toList();
        this.money.addAll(money);
    }
}