package com.menotifilho.desafio_api_pagamentos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @CPF
    @NotBlank
    @Column(unique = true)
    private String cpf;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    public User(String name,String cpf, String email , String password,Wallet wallet) {
        this.wallet = wallet;
        this.password = password;
        this.email = email;
        this.cpf = cpf;
        this.name = name;
    }
}
