package com.bettercloud.demo.axon.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by davidesposito on 4/10/17.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalance {

    @Id
    private String id;
    @Basic
    private int balance;
}
