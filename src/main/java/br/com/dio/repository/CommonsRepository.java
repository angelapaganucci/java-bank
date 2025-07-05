package br.com.dio.repository;

import br.com.dio.model.Wallet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


@Repository
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonsRepository {

    public static void checkFundsForTransaction(final Wallet source, final BigDecimal amount) {
        long amountInCents = amount.multiply(BigDecimal.valueOf(100)).longValueExact();

        if (source.getFunds() < amountInCents) {
            throw new br.com.dio.exception.NoFundsEnoughException("Fundos insuficientes para a transação.");
        }
    }

}
