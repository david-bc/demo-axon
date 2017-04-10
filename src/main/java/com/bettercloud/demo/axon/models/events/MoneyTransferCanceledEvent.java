package com.bettercloud.demo.axon.models.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Created by davidesposito on 4/9/17.
 */
@Data
@AllArgsConstructor
public class MoneyTransferCanceledEvent {

    @NonNull private String transferId;
}
