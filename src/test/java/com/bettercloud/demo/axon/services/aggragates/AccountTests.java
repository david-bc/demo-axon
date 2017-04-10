package com.bettercloud.demo.axon.services.aggragates;

import com.bettercloud.demo.axon.models.commands.DepositMoneyCommand;
import com.bettercloud.demo.axon.models.commands.WithdrawMoneyCommand;
import com.bettercloud.demo.axon.models.events.MoneyDepositedEvent;
import com.bettercloud.demo.axon.models.events.MoneyWithdrawnEvent;
import com.bettercloud.demo.axon.services.aggragates.Account;
import com.bettercloud.demo.axon.models.commands.CreateAccountCommand;
import com.bettercloud.demo.axon.models.events.AccountCreatedEvent;
import com.bettercloud.demo.axon.services.aggragates.OverdraftLimitExceededException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by davidesposito on 4/9/17.
 */
public class AccountTests {

    private AggregateTestFixture<Account> fixture;

    @Before
    public void setup() throws Exception {
        fixture = new AggregateTestFixture<>(Account.class);
    }

    @Test
    public void testCreateAccount() throws Exception {
        fixture.givenNoPriorActivity()
                .when(new CreateAccountCommand("1234", 1000))
                .expectEvents(new AccountCreatedEvent("1234", 1000));
    }

    @Test
    public void testWithdrawReasonableAmount() throws Exception {
        fixture.given(new AccountCreatedEvent("1234", 1000))
                .when(new WithdrawMoneyCommand("1234", "tf1", 600))
                .expectEvents(new MoneyWithdrawnEvent("1234", "tf1", 600, -600));
    }

    @Test
    public void testWithdrawAbsurdAmount() throws Exception {
        fixture.given(new AccountCreatedEvent("1234", 1000))
                .when(new WithdrawMoneyCommand("1234", "tf1", 1001))
                .expectNoEvents()
                .expectException(OverdraftLimitExceededException.class);
    }

    @Test
    public void testWithdrawTwice() throws Exception {
        fixture.given(new AccountCreatedEvent("1234", 1000),
                    new MoneyWithdrawnEvent("1234", "tf1", 999, -999)
                )
                .when(new WithdrawMoneyCommand("1234", "tf1", 2))
                .expectNoEvents()
                .expectException(OverdraftLimitExceededException.class);
    }

    @Test
    public void testDeposit() throws Exception {
        fixture.given(new AccountCreatedEvent("1234", 1000))
                .when(new DepositMoneyCommand("1234", "tf1", 100))
                .expectEvents(new MoneyDepositedEvent("1234", "tf1", 100, 100));
    }

    @Test
    public void testDepositAfterWithdraw() throws Exception {
        fixture.given(new AccountCreatedEvent("1234", 1000),
                    new MoneyWithdrawnEvent("1234", "tf1", 50, -50)
                )
                .when(new DepositMoneyCommand("1234", "tf1", 100))
                .expectEvents(new MoneyDepositedEvent("1234", "tf1", 100, 50));
    }

    @Test
    public void testLargeWithdrawAfterDeposit() throws Exception {
        fixture.given(new AccountCreatedEvent("1234", 1000),
                    new MoneyDepositedEvent("1234", "tf1", 1000, 1000)
                )
                .when(new WithdrawMoneyCommand("1234", "tf1", 1999))
                .expectEvents(new MoneyWithdrawnEvent("1234", "tf1", 1999, -999));
    }

    @Test
    public void testOverdrawAfterDeposit() throws Exception {
        fixture.given(new AccountCreatedEvent("1234", 1000),
                    new MoneyDepositedEvent("1234", "tf1", 1000, 1000)
                )
                .when(new WithdrawMoneyCommand("1234", "tf1", 2001))
                .expectNoEvents()
                .expectException(OverdraftLimitExceededException.class);;
    }
}
