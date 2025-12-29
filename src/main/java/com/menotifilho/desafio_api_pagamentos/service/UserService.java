package com.menotifilho.desafio_api_pagamentos.service;

import com.menotifilho.desafio_api_pagamentos.dto.UserDTO;
import com.menotifilho.desafio_api_pagamentos.dto.UserResponseDTO;
import com.menotifilho.desafio_api_pagamentos.exception.DataAlreadyExistsException;
import com.menotifilho.desafio_api_pagamentos.exception.UserNotFoundException;
import com.menotifilho.desafio_api_pagamentos.model.User;
import com.menotifilho.desafio_api_pagamentos.model.Wallet;
import com.menotifilho.desafio_api_pagamentos.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserDTO userDTO) {
        if (userRepository.findUserByCpf(userDTO.cpf()).isPresent()) {
            throw new DataAlreadyExistsException("CPF já cadastrado na base de dados.");
        }
        if (userRepository.findUserByEmail(userDTO.email()).isPresent()) {
            throw new DataAlreadyExistsException("Email já cadastrado na base de dados.");
        }

        Wallet newWallet = new Wallet(userDTO.balance());

        User newUser = new User(
                userDTO.name(),
                userDTO.cpf(),
                userDTO.email(),
                userDTO.password(),
                newWallet
        );

        return userRepository.save(newUser);
    }

    public List<UserResponseDTO> listAll(){
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::new)
                .toList();
    }

    public UserResponseDTO findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        return new UserResponseDTO(user);
    }
}
