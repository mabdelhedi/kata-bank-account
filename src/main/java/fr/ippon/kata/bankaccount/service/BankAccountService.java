package fr.ippon.kata.bankaccount.service;

import fr.ippon.kata.bankaccount.exception.BankAccountNotFoundException;
import fr.ippon.kata.bankaccount.exception.BankClientNotFoundException;
import fr.ippon.kata.bankaccount.model.Operation;
import fr.ippon.kata.bankaccount.model.OperationQuery;

import java.util.List;

public interface BankAccountService {

    Operation saveMoney(final OperationQuery operationQuery) throws BankAccountNotFoundException;
    Operation withdrawMoney(final OperationQuery operationQuery) throws BankAccountNotFoundException;
    List<Operation> getHistory(final String clientNumber) throws BankClientNotFoundException;

}
