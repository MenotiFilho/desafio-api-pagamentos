package com.menotifilho.desafio_api_pagamentos.model;

import com.menotifilho.desafio_api_pagamentos.exception.InsufficientBalanceException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private BigDecimal balance = BigDecimal.ZERO;

    public Wallet(BigDecimal balance) {
        this.balance = balance;
    }

    public void credit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void debit(BigDecimal amount) {

        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Saldo insuficiente para realizar a transação");
        }

        this.balance = this.balance.subtract(amount);
    }
}
