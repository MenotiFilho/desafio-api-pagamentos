package com.menotifilho.desafio_api_pagamentos.controller;

import com.menotifilho.desafio_api_pagamentos.dto.TransferDTO;
import com.menotifilho.desafio_api_pagamentos.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Object> performTransfer(@RequestBody TransferDTO transferDTO){
        transferService.transfer(transferDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
