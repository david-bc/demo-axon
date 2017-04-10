package com.bettercloud.demo.axon.models.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Created by davidesposito on 4/9/17.
 */
@Data
@AllArgsConstructor
public class MoneyWithdrawnEvent  {

    @NonNull private String accountId;
    private String transactionId;
    private int amount;
    private int balance;
}
