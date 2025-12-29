package com.menotifilho.desafio_api_pagamentos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferDTO(
        @NotNull
        Long payerId,

        @NotNull
        Long payeeId,

        @NotNull
        @Positive
        BigDecimal value) {
}
