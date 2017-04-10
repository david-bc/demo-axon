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
import org.axonframework.test.saga.SagaTestFixture;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

/**
 * Created by davidesposito on 4/10/17.
 */
public class MoneyTransferSagaTest {


    private SagaTestFixture fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new SagaTestFixture<>(MoneyTransferSaga.class);
    }

    @Test
    public void testMoneyTransferRequest() throws Exception {
         fixture.givenNoPriorActivity()
                 .whenPublishingA(new MoneyTransferRequestedEvent("tf1", "act1", "act2", 100))
                 .expectActiveSagas(1)
                 .expectDispatchedCommands(new WithdrawMoneyCommand("act1", "tf1", 100));
    }

    @Test
    public void testMoneyDepositAfterWithdrawn() throws Exception {
        fixture.givenAPublished(new MoneyTransferRequestedEvent("tf1", "act1", "act2", 100))
                .whenPublishingA(new MoneyWithdrawnEvent("act1", "tf1", 100, 400))
                .expectDispatchedCommands(new DepositMoneyCommand("act2", "tf1", 100));
    }

    @Test
    public void testCanceledAfterOverdraft() throws Exception {
        fixture.givenAPublished(new MoneyTransferRequestedEvent("tf1", "act1", "act2", 100))
                .whenPublishingA(new MoneyTransferCanceledEvent("tf1"))
                .expectActiveSagas(0);
    }

    @Test
    public void testCompleteAfterDeposit() throws Exception {
        fixture.givenAPublished(new MoneyTransferRequestedEvent("tf1", "act1", "act2", 100))
                .andThenAPublished(new MoneyWithdrawnEvent("act1", "tf1", 100, 400))
                .whenPublishingA(new MoneyDepositedEvent("act2", "tf1", 100, 500))
                .expectDispatchedCommands(new CompleteMoneyTransferCommand("tf1"));
    }

    @Test
    public void testSagaEndsAfterCompleted() throws Exception {
        fixture.givenAPublished(new MoneyTransferRequestedEvent("tf1", "act1", "act2", 100))
                .andThenAPublished(new MoneyWithdrawnEvent("act1", "tf1", 100, 400))
                .andThenAPublished(new MoneyDepositedEvent("act1", "tf1", 100, 500))
                .whenPublishingA(new MoneyTransferCompletedEvent("tf1"))
                .expectActiveSagas(0)
                .expectNoDispatchedCommands();
    }
}