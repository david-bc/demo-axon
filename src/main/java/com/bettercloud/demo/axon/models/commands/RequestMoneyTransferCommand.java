package com.bettercloud.demo.axon.models.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateIdentifier;

/**
 * Created by davidesposito on 4/9/17.
 */
@Data
@AllArgsConstructor
public class RequestMoneyTransferCommand {

    @NonNull
    @TargetAggregateIdentifier
    private String transferId;
    @NonNull private String sourceAccountId;
    @NonNull private String targetAccountId;
    private int amount;
}
