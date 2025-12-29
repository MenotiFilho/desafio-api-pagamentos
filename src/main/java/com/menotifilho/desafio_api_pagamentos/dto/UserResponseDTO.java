package com.menotifilho.desafio_api_pagamentos.dto;

import com.menotifilho.desafio_api_pagamentos.model.User;

import java.math.BigDecimal;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String cpf,
        BigDecimal balance
) {
    public UserResponseDTO(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCpf(),
                user.getWallet().getBalance()
        );
    }
}