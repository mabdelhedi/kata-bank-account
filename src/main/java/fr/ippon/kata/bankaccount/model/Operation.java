package fr.ippon.kata.bankaccount.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class Operation {

    @NonNull
    OperationType type;

    @NonNull
    LocalDateTime date;

    @NonNull
    BigDecimal amount;

    @NonNull
    BigDecimal balance;

}
