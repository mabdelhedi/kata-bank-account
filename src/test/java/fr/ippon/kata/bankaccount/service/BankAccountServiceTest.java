package fr.ippon.kata.bankaccount.service;

import fr.ippon.kata.bankaccount.exception.BankAccountNotFoundException;
import fr.ippon.kata.bankaccount.exception.BankClientNotFoundException;
import fr.ippon.kata.bankaccount.model.Account;
import fr.ippon.kata.bankaccount.model.BankClient;
import fr.ippon.kata.bankaccount.model.Operation;
import fr.ippon.kata.bankaccount.model.OperationQuery;
import fr.ippon.kata.bankaccount.model.OperationType;
import fr.ippon.kata.bankaccount.service.impl.BankAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankAccountServiceTest {

    private static final BankClient client1 = BankClient.builder().name("client1").number("123").build();
    private static final BankClient client2 = BankClient.builder().name("client2").number("456").build();
    private static final BankClient client3 = BankClient.builder().name("client3").number("789").build();

    private Account account1;

    private Account account2;

    private BankAccountService bankAccountService;

    @BeforeEach
    void beforeEach() {
        this.account1 = Account.builder()
                .holders(Set.of(client1, client2))
                .iban("123456789")
                .balance(BigDecimal.valueOf(20))
                .build();
        this.account2 = Account.builder()
                .holders(Set.of(client3))
                .iban("987654321")
                .build();
        List<Account> accounts = List.of(account1, account2);

        this.bankAccountService = new BankAccountServiceImpl(accounts);

    }

    @Test
    void saveMoney_shouldReturnOperation_whenValidOperationQuery() throws BankAccountNotFoundException {
        final OperationQuery operationQuery = OperationQuery.builder()
                .amount(BigDecimal.valueOf(35))
                .iban("123456789")
                .clientNumber("123")
                .build();

        LocalDateTime dateBeforeSaveMoney = LocalDateTime.now();

        Operation operation = bankAccountService.saveMoney(operationQuery);

        assertThat(operation).isNotNull();
        assertThat(operation.getType()).isEqualTo(OperationType.DEPOSIT);
        assertThat(operation.getAmount()).isEqualTo(BigDecimal.valueOf(35));
        assertThat(operation.getBalance()).isEqualTo(BigDecimal.valueOf(55));
        assertThat(operation.getDate()).isAfterOrEqualTo(dateBeforeSaveMoney);

        assertThat(account1.getBalance()).isEqualTo(operation.getBalance());
        assertThat(account1.getOperations()).containsOnly(operation);

    }

    @Test
    void saveMoney_shouldThrowBankAccountNotFoundException_whenInvalidClientNumber() {
        final OperationQuery operationQuery = OperationQuery.builder()
                .amount(BigDecimal.valueOf(35))
                .iban("123456789")
                .clientNumber("1234")
                .build();

        BankAccountNotFoundException exception = assertThrows(BankAccountNotFoundException.class,
                () -> bankAccountService.saveMoney(operationQuery));

        assertThat(exception.getIban()).isEqualTo(operationQuery.getIban());
        assertThat(exception.getClientNumber()).isEqualTo(operationQuery.getClientNumber());
    }

    @Test
    void withdrawMoney_shouldReturnOperation_whenValidOperationQuery() throws BankAccountNotFoundException {
        final OperationQuery operationQuery = OperationQuery.builder()
                .amount(BigDecimal.valueOf(15))
                .iban("987654321")
                .clientNumber("789")
                .build();

        LocalDateTime dateBeforeWithdrawMoney = LocalDateTime.now();

        Operation operation = bankAccountService.withdrawMoney(operationQuery);

        assertThat(operation).isNotNull();
        assertThat(operation.getType()).isEqualTo(OperationType.WITHDRAWAL);
        assertThat(operation.getAmount()).isEqualTo(BigDecimal.valueOf(15));
        assertThat(operation.getBalance()).isEqualTo(BigDecimal.valueOf(-15));
        assertThat(operation.getDate()).isAfterOrEqualTo(dateBeforeWithdrawMoney);

        assertThat(account2.getBalance()).isEqualTo(operation.getBalance());
        assertThat(account2.getOperations()).containsOnly(operation);
    }

    @Test
    void withdrawMoney_shouldThrowBankAccountNotFoundException_whenInvalidIban() {
        final OperationQuery operationQuery = OperationQuery.builder()
                .amount(BigDecimal.valueOf(15))
                .iban("987654321")
                .clientNumber("123")
                .build();

        BankAccountNotFoundException exception = assertThrows(BankAccountNotFoundException.class,
                () -> bankAccountService.withdrawMoney(operationQuery));

        assertThat(exception.getIban()).isEqualTo(operationQuery.getIban());
        assertThat(exception.getClientNumber()).isEqualTo(operationQuery.getClientNumber());
    }

    @Test
    void getHistory_shouldReturnOperations_whenValidClientNumber() throws BankClientNotFoundException {

        final String clientNumber = "456";

        account1.setBalance(BigDecimal.valueOf(40));
        account1.setOperations(Set.of(Operation.builder()
                        .type(OperationType.DEPOSIT)
                        .amount(BigDecimal.valueOf(23))
                        .balance(BigDecimal.valueOf(43))
                        .date(LocalDateTime.of(2021, 5, 21, 15, 50))
                        .build(),
                Operation.builder()
                        .type(OperationType.WITHDRAWAL)
                        .amount(BigDecimal.valueOf(12))
                        .balance(BigDecimal.valueOf(31))
                        .date(LocalDateTime.of(2021, 5, 22, 11, 30))
                        .build(),
                Operation.builder()
                        .type(OperationType.DEPOSIT)
                        .amount(BigDecimal.valueOf(9))
                        .balance(BigDecimal.valueOf(40))
                        .date(LocalDateTime.of(2021, 5, 24, 18, 22))
                        .build()));

        List<Operation> operations = bankAccountService.getHistory(clientNumber);

        assertThat(operations).hasSize(3);
        assertThat(operations).containsExactlyElementsOf(account1.getOperations());

    }

    @Test
    void getHistory_shouldThrowBankClientNotFoundException_whenInvalidClientNumber() {

        final String clientNumber = "45689";

        BankClientNotFoundException bankClientNotFoundException = assertThrows(BankClientNotFoundException.class,
                () -> bankAccountService.getHistory(clientNumber));

        assertThat(bankClientNotFoundException.getClientNumber()).isEqualTo(clientNumber);

    }

}