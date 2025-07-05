package br.com.dio.model;

import br.com.dio.Utils;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class Wallet {

    @Getter
    private final BankService service;

    protected final List<Money> money;

    protected Wallet(BankService serviceType) {
        this.service = serviceType;
        this.money = new ArrayList<>();
    }

    public List<Money> generateMoney(final BigDecimal amount, final String description) {
        var cents = amount.multiply(BigDecimal.valueOf(100)).longValueExact();
        var history = new MoneyAudit(UUID.randomUUID(), service, description, OffsetDateTime.now());
        return Stream.generate(() -> new Money(history))
                .limit(cents)
                .toList();
    }

    public long getFunds() {
        return money.size();
    }

    public void addMoney(final List<Money> money, final BankService service, final String description) {
        var history = new MoneyAudit(UUID.randomUUID(), service, description, OffsetDateTime.now());
        money.forEach(m -> m.addHistory(history));
        this.money.addAll(money);
    }

    public List<Money> reduceMoney(final BigDecimal amount) {
        List<Money> toRemove = new ArrayList<>();

        long amountInCents = amount.multiply(BigDecimal.valueOf(100)).longValueExact();

        for (int i = 0; i < amountInCents; i++) {
            toRemove.add(this.money.remove(0));
        }

        return toRemove;
    }

    public List<MoneyAudit> getFinancialTransactions() {
        return this.money.stream()
                .flatMap(m -> m.getHistory().stream())
                .toList();
    }

    @Override
    public String toString() {
        var total = BigDecimal.valueOf(money.size()).divide(BigDecimal.valueOf(100));
        return String.format("Wallet{service=%s, money= %s}", service, Utils.formatCurrency(total));
    }

}