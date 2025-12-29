package com.menotifilho.desafio_api_pagamentos.exception;

public class TransferNotAllowedException extends  RuntimeException {

    public TransferNotAllowedException(String message) {
        super(message);
    }

}
