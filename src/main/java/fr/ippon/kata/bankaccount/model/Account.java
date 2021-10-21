package fr.ippon.kata.bankaccount.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Account {

    @NonNull
    String iban;

    @Builder.Default
    BigDecimal balance = BigDecimal.valueOf(0);

    @Builder.Default
    Set<BankClient> holders = new HashSet<>();

    @Builder.Default
    Set<Operation> operations = new HashSet<>();

}
