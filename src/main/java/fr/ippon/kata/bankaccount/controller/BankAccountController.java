package fr.ippon.kata.bankaccount.controller;

import fr.ippon.kata.bankaccount.exception.BankAccountNotFoundException;
import fr.ippon.kata.bankaccount.exception.BankClientNotFoundException;
import fr.ippon.kata.bankaccount.model.Operation;
import fr.ippon.kata.bankaccount.model.OperationQuery;
import fr.ippon.kata.bankaccount.service.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/v1/bankAccount")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping("/save")
    public ResponseEntity<Operation> saveMoney(@RequestBody OperationQuery operationQuery) {
        try {
            return ResponseEntity.ok(bankAccountService.saveMoney(operationQuery));
        } catch (BankAccountNotFoundException e) {
            log.warn(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Operation> withdrawMoney(@RequestBody OperationQuery operationQuery) {
        try {
            return ResponseEntity.ok(bankAccountService.withdrawMoney(operationQuery));
        } catch (BankAccountNotFoundException e) {
            log.warn(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/history")
    public ResponseEntity<List<Operation>> getHistory(@RequestParam final String clientNumber) {
        try {
            return ResponseEntity.ok(bankAccountService.getHistory(clientNumber));
        } catch (BankClientNotFoundException e) {
            log.warn(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
