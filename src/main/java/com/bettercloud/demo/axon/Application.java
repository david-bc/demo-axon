package com.bettercloud.demo.axon;

import com.bettercloud.demo.axon.models.commands.CreateAccountCommand;
import com.bettercloud.demo.axon.models.commands.RequestMoneyTransferCommand;
import com.bettercloud.demo.axon.models.commands.WithdrawMoneyCommand;
import com.bettercloud.demo.axon.services.aggragates.Account;
import org.axonframework.commandhandling.AsynchronousCommandBus;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.model.GenericJpaRepository;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.spring.config.EnableAxon;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.PlatformTransactionManager;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

@EntityScan
@EnableAxon
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public Repository<Account> accountRepo(EventBus eventBus) {
		return new GenericJpaRepository<>(entityManagerProvider(), Account.class, eventBus);
	}

	@Bean
	public EntityManagerProvider entityManagerProvider() {
		return new ContainerManagedEntityManagerProvider();
	}

	@Bean
	public EventStorageEngine eventStorageEngine() {
		return new InMemoryEventStorageEngine();
	}

	@Bean
	public TransactionManager axonTransactionManager(PlatformTransactionManager tx) {
		return new SpringTransactionManager(tx);
	}

	@Bean
	public CommandLineRunner initRunner(CommandBus commandBus) {
		return args -> {
			commandBus.dispatch(asCommandMessage(new CreateAccountCommand("1", 1000)));
			commandBus.dispatch(asCommandMessage(new CreateAccountCommand("2", 2000)));
			commandBus.dispatch(asCommandMessage(new CreateAccountCommand("3", 3000)));
//			commandBus.dispatch(asCommandMessage(new RequestMoneyTransferCommand("tf1", "1", "2", 100)));
//			commandBus.dispatch(asCommandMessage(new RequestMoneyTransferCommand("tf1", "2", "3", 1000)));
		};
	}
}
