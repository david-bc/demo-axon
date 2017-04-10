package com.bettercloud.demo.axon.port.rest.inbound;

import com.bettercloud.demo.axon.models.AccountBalance;
import com.bettercloud.demo.axon.models.commands.RequestMoneyTransferCommand;
import com.bettercloud.demo.axon.repositories.AccountBalanceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.axonframework.commandhandling.CommandBus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

/**
 * Created by davidesposito on 4/10/17.
 */
@RestController
@RequestMapping("/balances")
public class AccountBalanceController {

    private final AccountBalanceRepository accountBalanceRepo;
    private final CommandBus commandBus;

    public AccountBalanceController(AccountBalanceRepository accountBalanceRepo, CommandBus commandBus) {
        this.accountBalanceRepo = accountBalanceRepo;
        this.commandBus = commandBus;
    }

    @GetMapping
    public ResponseEntity<List<AccountBalance>> findAll() {
        return ResponseEntity.ok(accountBalanceRepo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountBalance> findOne(@PathVariable("id")  String id) {
        return Optional.ofNullable(accountBalanceRepo.findOne(id))
                .map(bal -> ResponseEntity.ok(bal))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null)
                );
    }

    @PostMapping("/transfer")
    public ResponseEntity<Boolean> transfer(@RequestBody JsonNode transferRequest) {
        if (!transferRequest.path("source").isTextual() || !transferRequest.path("target").isTextual() || !transferRequest.path("amount").isNumber()) {
            return ResponseEntity.badRequest().body(false);
        }
        commandBus.dispatch(asCommandMessage(new RequestMoneyTransferCommand(
                UUID.randomUUID().toString(),
                transferRequest.get("source").asText(),
                transferRequest.path("target").asText(),
                transferRequest.path("amount").asInt()
        )));
        return ResponseEntity.ok(true);
    }
}
