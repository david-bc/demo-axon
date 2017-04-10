package com.bettercloud.demo.axon.models.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Created by davidesposito on 4/9/17.
 */
@Data
@AllArgsConstructor
public class MoneyTransferRequestedEvent {

    @NonNull private String transferId;
    @NonNull private String sourceAccountId;
    @NonNull private String targetAccountId;
    private int amount;
}
