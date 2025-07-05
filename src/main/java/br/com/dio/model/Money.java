package br.com.dio.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class Money {

    private final List<MoneyAudit> history = new ArrayList<>();

    public Money(MoneyAudit history) {
        this.history.add(history);
    }

    public void addHistory(MoneyAudit history) {
        this.history.add(history);
    }
}
