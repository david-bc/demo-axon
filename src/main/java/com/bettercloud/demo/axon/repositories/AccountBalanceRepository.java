package com.bettercloud.demo.axon.repositories;

import com.bettercloud.demo.axon.models.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by davidesposito on 4/10/17.
 */
public interface AccountBalanceRepository extends JpaRepository<AccountBalance, String> {
}
