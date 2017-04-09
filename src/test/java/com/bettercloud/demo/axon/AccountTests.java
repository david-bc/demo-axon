package com.bettercloud.demo.axon;

import com.bettercloud.demo.axon.models.commands.WithdrawMoneyCommand;
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
                .when(new WithdrawMoneyCommand("1234", 600))
                .expectEvents(new MoneyWithdrawnEvent("1234", 600, -600));
    }

    @Test
    public void testWithdrawAbsurdAmount() throws Exception {
        fixture.given(new AccountCreatedEvent("1234", 1000))
                .when(new WithdrawMoneyCommand("1234", 1001))
                .expectNoEvents()
                .expectException(OverdraftLimitExceededException.class);
    }

    @Test
    public void testWithdrawTwice() throws Exception {
        fixture.given(new AccountCreatedEvent("1234", 1000),
                    new MoneyWithdrawnEvent("1234", 999, -999)
                )
                .when(new WithdrawMoneyCommand("1234", 2))
                .expectNoEvents()
                .expectException(OverdraftLimitExceededException.class);
    }
}
