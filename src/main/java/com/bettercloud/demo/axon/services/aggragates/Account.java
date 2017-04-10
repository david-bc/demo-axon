package com.bettercloud.demo.axon.services.aggragates;

import com.bettercloud.demo.axon.models.commands.CreateAccountCommand;
import com.bettercloud.demo.axon.models.commands.DepositMoneyCommand;
import com.bettercloud.demo.axon.models.commands.WithdrawMoneyCommand;
import com.bettercloud.demo.axon.models.events.AccountCreatedEvent;
import com.bettercloud.demo.axon.models.events.MoneyDepositedEvent;
import com.bettercloud.demo.axon.models.events.MoneyWithdrawnEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

/**
 * Created by davidesposito on 4/9/17.
 */
@Aggregate(repository = "accountRepo")
@NoArgsConstructor
@Entity
public class Account {

    @Id
    @AggregateIdentifier
    private String accountId;
    @Basic
    private int balance;
    @Basic
    private int overdraftLimit;

    @CommandHandler
    public Account(CreateAccountCommand cmd) {
         apply(new AccountCreatedEvent(cmd.getAccountId(), cmd.getOverdraftLimit()));
    }

    @CommandHandler
    public void handle(WithdrawMoneyCommand cmd) throws OverdraftLimitExceededException {
        if (this.balance + this.overdraftLimit >= cmd.getAmount()) {
            apply(new MoneyWithdrawnEvent(this.accountId, cmd.getTransactionId(), cmd.getAmount(), balance - cmd.getAmount()));
        } else {
            throw new OverdraftLimitExceededException();
        }
    }

    @CommandHandler
    public void handle(DepositMoneyCommand cmd) {
        apply(new MoneyDepositedEvent(this.accountId, cmd.getTransactionId(), cmd.getAmount(), balance + cmd.getAmount()));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent e) {
        this.accountId = e.getAccountId();
        this.overdraftLimit = e.getOverdraftLimit();
    }

    @EventSourcingHandler
    public void on(MoneyWithdrawnEvent e) {
        this.balance = e.getBalance();
    }

    @EventSourcingHandler
    public void on(MoneyDepositedEvent e) {
        this.balance = e.getBalance();
    }
}
