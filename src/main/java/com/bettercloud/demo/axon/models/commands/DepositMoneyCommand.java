package com.bettercloud.demo.axon.models.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

/**
 * Created by davidesposito on 4/9/17.
 */
@Data
@AllArgsConstructor
public class DepositMoneyCommand {

    @NonNull
    @TargetAggregateIdentifier
    private String accountId;
    private String transactionId;
    private int amount;
}
