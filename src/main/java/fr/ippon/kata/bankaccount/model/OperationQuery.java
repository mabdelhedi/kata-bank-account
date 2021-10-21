package fr.ippon.kata.bankaccount.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@Builder
public class OperationQuery {

    @NonNull
    String iban;
    @NonNull
    String clientNumber;
    @NonNull
    BigDecimal amount;

}
