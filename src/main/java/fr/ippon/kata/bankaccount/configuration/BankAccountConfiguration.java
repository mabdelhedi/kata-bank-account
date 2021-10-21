package fr.ippon.kata.bankaccount.configuration;

import fr.ippon.kata.bankaccount.model.Account;
import fr.ippon.kata.bankaccount.model.BankClient;
import fr.ippon.kata.bankaccount.service.BankAccountService;
import fr.ippon.kata.bankaccount.service.impl.BankAccountServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Configuration
public class BankAccountConfiguration {

    @Bean
    public BankAccountService bankAccountService() {
        final BankClient client1 = BankClient.builder().name("client1").number("123").build();
        final BankClient client2 = BankClient.builder().name("client2").number("456").build();
        final BankClient client3 = BankClient.builder().name("client3").number("789").build();
        final Account account1 = Account.builder()
                .holders(Set.of(client1, client2))
                .iban("123456789")
                .balance(BigDecimal.valueOf(20))
                .build();
        final Account account2 = Account.builder()
                .holders(Set.of(client3))
                .iban("987654321")
                .build();

        final List<Account> accounts = List.of(account1, account2);

        return new BankAccountServiceImpl(accounts);
    }
}
