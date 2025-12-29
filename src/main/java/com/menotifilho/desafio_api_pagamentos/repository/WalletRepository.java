package com.menotifilho.desafio_api_pagamentos.repository;

import com.menotifilho.desafio_api_pagamentos.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
}
