package fr.ippon.kata.bankaccount.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class BankClient {

    @NonNull
    String number;

    String name;

}
