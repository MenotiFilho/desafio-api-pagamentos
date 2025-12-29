package com.menotifilho.desafio_api_pagamentos.service;

import com.menotifilho.desafio_api_pagamentos.dto.TransferDTO;
import com.menotifilho.desafio_api_pagamentos.exception.TransferNotAllowedException;
import com.menotifilho.desafio_api_pagamentos.exception.UserNotFoundException;
import com.menotifilho.desafio_api_pagamentos.model.User;
import com.menotifilho.desafio_api_pagamentos.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransferService {

    private final UserRepository userRepository;

    public TransferService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void transfer(TransferDTO transferDTO){
        if (transferDTO.value().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransferNotAllowedException("Valor da transferência deve ser maior do que 0.0");
        }
        User payer = userRepository.findById(transferDTO.payerId()).orElseThrow(() -> new UserNotFoundException("Pagador não encontrado"));
        User payee = userRepository.findById(transferDTO.payeeId()).orElseThrow(() -> new UserNotFoundException("Recebedor não encontrado"));
        if (payer.getId().equals(payee.getId())) {
            throw new TransferNotAllowedException("Não é possível tranferir para o mesmo CPF");
        }

        payer.getWallet().debit(transferDTO.value());
        payee.getWallet().credit(transferDTO.value());

        userRepository.save(payer);
        userRepository.save(payee);
    }

}
