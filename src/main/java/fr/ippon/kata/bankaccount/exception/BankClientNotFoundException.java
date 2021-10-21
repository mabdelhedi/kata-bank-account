package fr.ippon.kata.bankaccount.exception;

import lombok.Getter;

import static java.lang.String.format;

@Getter
public class BankClientNotFoundException extends Exception{

    private final String clientNumber;

    public BankClientNotFoundException(final String clientNumber) {
        super(format("Client %s not found", clientNumber));
        this.clientNumber = clientNumber;
    }
}
