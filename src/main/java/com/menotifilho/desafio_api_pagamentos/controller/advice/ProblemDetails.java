package com.menotifilho.desafio_api_pagamentos.controller.advice;

import java.time.LocalDateTime;

public record ProblemDetails(int status, String type, String title, String detail, LocalDateTime timestamp) {
}