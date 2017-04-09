package com.bettercloud.demo.axon.models.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Created by davidesposito on 4/9/17.
 */
@Data
@AllArgsConstructor
public class CreateAccountCommand {

    @NonNull private String accountId;
    private int overdraftLimit;
}
