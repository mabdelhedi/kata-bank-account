package fr.ippon.kata.bankaccount.service.impl;

import fr.ippon.kata.bankaccount.exception.BankAccountNotFoundException;
import fr.ippon.kata.bankaccount.exception.BankClientNotFoundException;
import fr.ippon.kata.bankaccount.model.Account;
import fr.ippon.kata.bankaccount.model.BankClient;
import fr.ippon.kata.bankaccount.model.Operation;
import fr.ippon.kata.bankaccount.model.OperationQuery;
import fr.ippon.kata.bankaccount.model.OperationType;
import fr.ippon.kata.bankaccount.service.BankAccountService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

public class BankAccountServiceImpl implements BankAccountService {

    private List<Account> accounts;

    public BankAccountServiceImpl(final List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public Operation saveMoney(OperationQuery operationQuery) throws BankAccountNotFoundException {
        Account bankAccount = accounts.stream()
                .filter(account -> account.getIban().equals(operationQuery.getIban()))
                .filter(account -> account.getHolders().stream().map(BankClient::getNumber).anyMatch(number -> number.equals(operationQuery.getClientNumber())))
                .findFirst()
                .orElseThrow(() -> new BankAccountNotFoundException(operationQuery.getIban(), operationQuery.getClientNumber()));

        BigDecimal totalBalance = bankAccount.getBalance().add(operationQuery.getAmount());

        Operation operation = Operation.builder()
                .date(LocalDateTime.now())
                .balance(totalBalance)
                .amount(operationQuery.getAmount())
                .type(OperationType.DEPOSIT)
                .build();

        bankAccount.getOperations().add(operation);

        bankAccount.setBalance(totalBalance);

        return operation;
    }

    @Override
    public Operation withdrawMoney(OperationQuery operationQuery) throws BankAccountNotFoundException {
        Account bankAccount = accounts.stream()
                .filter(account -> account.getIban().equals(operationQuery.getIban()))
                .filter(account -> account.getHolders().stream().map(BankClient::getNumber).anyMatch(number -> number.equals(operationQuery.getClientNumber())))
                .findFirst()
                .orElseThrow(() -> new BankAccountNotFoundException(operationQuery.getIban(), operationQuery.getClientNumber()));

        BigDecimal totalBalance = bankAccount.getBalance().subtract(operationQuery.getAmount());

        Operation operation = Operation.builder()
                .date(LocalDateTime.now())
                .balance(totalBalance)
                .amount(operationQuery.getAmount())
                .type(OperationType.WITHDRAWAL)
                .build();

        bankAccount.getOperations().add(operation);

        bankAccount.setBalance(totalBalance);

        return operation;
    }

    @Override
    public List<Operation> getHistory(String clientNumber) throws BankClientNotFoundException {
        List<Account> clientAccounts = this.accounts.stream()
                .filter(account -> account.getHolders().stream().map(BankClient::getNumber).anyMatch(clientNumber::equals))
                .collect(Collectors.toList());

        if (isEmpty(clientAccounts)) {
            throw new BankClientNotFoundException(clientNumber);
        }

        return clientAccounts.stream()
                .map(Account::getOperations)
                .flatMap(Set::stream)
                .collect(Collectors.toList());
    }
}
