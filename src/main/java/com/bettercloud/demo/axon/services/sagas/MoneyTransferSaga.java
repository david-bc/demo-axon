package com.bettercloud.demo.axon.services.sagas;

import com.bettercloud.demo.axon.models.commands.CancelMoneyTransferCommand;
import com.bettercloud.demo.axon.models.commands.CompleteMoneyTransferCommand;
import com.bettercloud.demo.axon.models.commands.DepositMoneyCommand;
import com.bettercloud.demo.axon.models.commands.WithdrawMoneyCommand;
import com.bettercloud.demo.axon.models.events.MoneyDepositedEvent;
import com.bettercloud.demo.axon.models.events.MoneyTransferCanceledEvent;
import com.bettercloud.demo.axon.models.events.MoneyTransferCompletedEvent;
import com.bettercloud.demo.axon.models.events.MoneyTransferRequestedEvent;
import com.bettercloud.demo.axon.models.events.MoneyWithdrawnEvent;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.saga.EndSaga;
import org.axonframework.eventhandling.saga.SagaEventHandler;
import org.axonframework.eventhandling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by davidesposito on 4/10/17.
 */
@Saga
public class MoneyTransferSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    private String targetAccountId;

    @StartSaga
    @SagaEventHandler(associationProperty = "transferId")
    public void on(MoneyTransferRequestedEvent e) {
        this.targetAccountId = e.getTargetAccountId();
        commandGateway.send(new WithdrawMoneyCommand(e.getSourceAccountId(), e.getTransferId(), e.getAmount()), new CommandCallback<WithdrawMoneyCommand, Object>() {
            @Override
            public void onSuccess(CommandMessage<? extends WithdrawMoneyCommand> commandMessage, Object result) {
                // do nothing
            }

            @Override
            public void onFailure(CommandMessage<? extends WithdrawMoneyCommand> commandMessage, Throwable cause) {
                commandGateway.send(new CancelMoneyTransferCommand(e.getTransferId()));
            }
        });
    }

    @SagaEventHandler(associationProperty = "transactionId", keyName = "transferId")
    public void on(MoneyWithdrawnEvent e) {
        commandGateway.send(new DepositMoneyCommand(this.targetAccountId, e.getTransactionId(), e.getAmount()));
    }

    @SagaEventHandler(associationProperty = "transactionId", keyName = "transferId")
    public void on(MoneyDepositedEvent e) {
        commandGateway.send(new CompleteMoneyTransferCommand(e.getTransactionId()));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "transferId")
    public void on(MoneyTransferCompletedEvent e) { }

    @EndSaga
    @SagaEventHandler(associationProperty = "transferId")
    public void on(MoneyTransferCanceledEvent e) { }
}
