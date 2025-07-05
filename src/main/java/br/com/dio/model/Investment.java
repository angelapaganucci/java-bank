package br.com.dio.model;

import br.com.dio.Utils;

import java.math.BigDecimal;

public record Investment(long id,
                         long tax,
                         BigDecimal initialFunds) {
    @Override
    public String toString() {
        return "Investment{" +
                "id=" + id +
                ", tax=" + tax + "%" +
                ", initialFunds=" + Utils.formatCurrency(initialFunds) +
                '}';
    }
}