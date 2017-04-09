package com.bettercloud.demo.axon;

import com.bettercloud.demo.axon.models.commands.CreateAccountCommand;
import com.bettercloud.demo.axon.models.commands.WithdrawMoneyCommand;
import com.bettercloud.demo.axon.services.aggragates.Account;
import org.axonframework.config.Configuration;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

/**
 * Created by davidesposito on 4/9/17.
 */
public class DefaultApplication {

    public static void main(String[] args) {
        Configuration config = DefaultConfigurer.defaultConfiguration()
                .configureAggregate(Account.class)
                .configureEmbeddedEventStore(c -> new InMemoryEventStorageEngine())
//                .configureCommandBus(c -> new AsynchronousCommandBus())
                .buildConfiguration();

        config.start();
        config.commandBus().dispatch(asCommandMessage(new CreateAccountCommand("1234", 1000)));
        config.commandBus().dispatch(asCommandMessage(new WithdrawMoneyCommand("1234", 500)));
        config.commandBus().dispatch(asCommandMessage(new WithdrawMoneyCommand("1234", 501)));
    }
}
