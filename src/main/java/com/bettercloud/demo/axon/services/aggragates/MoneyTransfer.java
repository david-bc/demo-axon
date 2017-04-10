package com.bettercloud.demo.axon.services.aggragates;

import com.bettercloud.demo.axon.models.commands.CancelMoneyTransferCommand;
import com.bettercloud.demo.axon.models.commands.CompleteMoneyTransferCommand;
import com.bettercloud.demo.axon.models.commands.CreateAccountCommand;
import com.bettercloud.demo.axon.models.commands.DepositMoneyCommand;
import com.bettercloud.demo.axon.models.commands.RequestMoneyTransferCommand;
import com.bettercloud.demo.axon.models.commands.WithdrawMoneyCommand;
import com.bettercloud.demo.axon.models.events.AccountCreatedEvent;
import com.bettercloud.demo.axon.models.events.MoneyDepositedEvent;
import com.bettercloud.demo.axon.models.events.MoneyTransferCanceledEvent;
import com.bettercloud.demo.axon.models.events.MoneyTransferCompletedEvent;
import com.bettercloud.demo.axon.models.events.MoneyTransferRequestedEvent;
import com.bettercloud.demo.axon.models.events.MoneyWithdrawnEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.axonframework.commandhandling.model.AggregateLifecycle.markDeleted;

/**
 * Created by davidesposito on 4/9/17.
 */
@Slf4j
@Entity
@Aggregate
@NoArgsConstructor
public class MoneyTransfer {

    @Id
    @AggregateIdentifier
    private String transferId;

    @CommandHandler
    public MoneyTransfer(RequestMoneyTransferCommand cmd) {
        log.info(cmd.toString());
        apply(new MoneyTransferRequestedEvent(cmd.getTransferId(), cmd.getSourceAccountId(), cmd.getTargetAccountId(), cmd.getAmount()));
    }

    @CommandHandler
    public void handle(CompleteMoneyTransferCommand cmd) throws OverdraftLimitExceededException {
        log.info(cmd.toString());
        apply(new MoneyTransferCompletedEvent(this.transferId));
    }

    @CommandHandler
    public void handle(CancelMoneyTransferCommand cmd) {
        log.info(cmd.toString());
        apply(new MoneyTransferCanceledEvent(this.transferId));
    }

    @EventSourcingHandler
    public void on(MoneyTransferRequestedEvent e) {
        this.transferId = e.getTransferId();
    }

    @EventSourcingHandler
    public void on(MoneyTransferCompletedEvent e) {
        markDeleted();
    }
}
