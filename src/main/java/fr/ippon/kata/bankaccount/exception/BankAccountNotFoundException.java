package fr.ippon.kata.bankaccount.exception;

import lombok.Getter;

import static java.lang.String.format;

@Getter
public class BankAccountNotFoundException extends Exception{

    private final String iban;
    private final String clientNumber;

    public BankAccountNotFoundException(final String iban, final String clientNumber) {
        super(format("Account %s for user %s not found", iban, clientNumber));
        this.iban = iban;
        this.clientNumber = clientNumber;
    }

}
