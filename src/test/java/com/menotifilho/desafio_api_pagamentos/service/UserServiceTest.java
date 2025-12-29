package com.menotifilho.desafio_api_pagamentos.service;

import com.menotifilho.desafio_api_pagamentos.dto.UserDTO;
import com.menotifilho.desafio_api_pagamentos.dto.UserResponseDTO;
import com.menotifilho.desafio_api_pagamentos.exception.DataAlreadyExistsException;
import com.menotifilho.desafio_api_pagamentos.exception.UserNotFoundException;
import com.menotifilho.desafio_api_pagamentos.model.User;
import com.menotifilho.desafio_api_pagamentos.model.Wallet;
import com.menotifilho.desafio_api_pagamentos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve criar usuário com sucesso quando dados forem válidos")
    void shouldCreateUserSuccessfully() {
        UserDTO userDTO = new UserDTO("João", "joao@email.com", "12345678900", "senha123", new BigDecimal("100.00"));
        User expectedUser = new User(userDTO.name(), userDTO.cpf(), userDTO.email(), userDTO.password(), new Wallet(userDTO.balance()));

        when(userRepository.findUserByCpf(userDTO.cpf())).thenReturn(Optional.empty());
        when(userRepository.findUserByEmail(userDTO.email())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        User createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals(userDTO.name(), createdUser.getName());
        assertEquals(userDTO.cpf(), createdUser.getCpf());
        assertEquals(userDTO.balance(), createdUser.getWallet().getBalance());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar usuário com CPF já existente")
    void shouldThrowExceptionWhenCpfAlreadyExists() {
        UserDTO userDTO = new UserDTO("João", "joao@email.com", "12345678900", "senha123", new BigDecimal("100.00"));
        User existingUser = new User();

        when(userRepository.findUserByCpf(userDTO.cpf())).thenReturn(Optional.of(existingUser));

        assertThrows(DataAlreadyExistsException.class, () -> userService.createUser(userDTO));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar usuário com Email já existente")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        UserDTO userDTO = new UserDTO("João", "joao@email.com", "12345678900", "senha123", new BigDecimal("100.00"));
        User existingUser = new User();

        when(userRepository.findUserByCpf(userDTO.cpf())).thenReturn(Optional.empty());
        when(userRepository.findUserByEmail(userDTO.email())).thenReturn(Optional.of(existingUser));

        assertThrows(DataAlreadyExistsException.class, () -> userService.createUser(userDTO));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve retornar lista de usuários convertida para DTO")
    void shouldListAllUsers() {
        User user1 = new User("User1", "111", "user1@email.com", "123", new Wallet(BigDecimal.TEN));
        User user2 = new User("User2", "222", "user2@email.com", "123", new Wallet(BigDecimal.TEN));
        List<User> userList = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<UserResponseDTO> result = userService.listAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("User1", result.get(0).name());
        assertEquals("User2", result.get(1).name());
    }

    @Test
    @DisplayName("Deve encontrar usuário por ID e retornar DTO")
    void shouldFindUserByIdSuccessfully() {
        Long userId = 1L;
        User user = new User("User1", "111", "user1@email.com", "123", new Wallet(BigDecimal.TEN));
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.findUserById(userId);

        assertNotNull(result);
        assertEquals(user.getName(), result.name());
        assertEquals(user.getCpf(), result.cpf());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado por ID")
    void shouldThrowExceptionWhenUserNotFoundById() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(userId));
    }
}