package com.menotifilho.desafio_api_pagamentos.exception;

public class DataAlreadyExistsException extends RuntimeException{
    public DataAlreadyExistsException(String message) {
        super(message);
    }
}
