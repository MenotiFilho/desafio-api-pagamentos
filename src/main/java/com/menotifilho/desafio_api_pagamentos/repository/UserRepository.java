package com.menotifilho.desafio_api_pagamentos.repository;

import com.menotifilho.desafio_api_pagamentos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByCpf(String cpf);

    Optional<User> findUserByEmail(String email);
}
