package com.menotifilho.desafio_api_pagamentos.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

public record UserDTO(
        @NotBlank
        String name,
        @NotBlank
        @Email
        String email,

        @NotBlank
        @CPF
        String cpf,

        @NotBlank
        @Size(min = 6)
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password,

        BigDecimal balance) {
}
