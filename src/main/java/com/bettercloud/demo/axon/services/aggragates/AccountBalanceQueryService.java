package com.bettercloud.demo.axon.services.aggragates;

import com.bettercloud.demo.axon.models.AccountBalance;
import com.bettercloud.demo.axon.models.events.AccountCreatedEvent;
import com.bettercloud.demo.axon.models.events.MoneyDepositedEvent;
import com.bettercloud.demo.axon.models.events.MoneyWithdrawnEvent;
import com.bettercloud.demo.axon.repositories.AccountBalanceRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

/**
 * Created by davidesposito on 4/10/17.
 */
@Service
public class AccountBalanceQueryService {

    private final AccountBalanceRepository accountBalanceRepo;

    public AccountBalanceQueryService(AccountBalanceRepository accountBalanceRepo) {
        this.accountBalanceRepo = accountBalanceRepo;
    }

    @EventHandler
    public void on(AccountCreatedEvent e) {
        accountBalanceRepo.save(AccountBalance.builder()
                .id(e.getAccountId())
                .balance(0)
                .build());
    }

    @EventHandler
    public void on(MoneyWithdrawnEvent e) {
        accountBalanceRepo.save(AccountBalance.builder()
                .id(e.getAccountId())
                .balance(e.getBalance())
                .build());
    }

    @EventHandler
    public void on(MoneyDepositedEvent e) {
        accountBalanceRepo.save(AccountBalance.builder()
                .id(e.getAccountId())
                .balance(e.getBalance())
                .build());
    }
}
