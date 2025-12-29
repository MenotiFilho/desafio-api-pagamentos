package com.menotifilho.desafio_api_pagamentos.service;

import com.menotifilho.desafio_api_pagamentos.dto.TransferDTO;
import com.menotifilho.desafio_api_pagamentos.exception.InsufficientBalanceException;
import com.menotifilho.desafio_api_pagamentos.exception.TransferNotAllowedException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransferService transferService;

    @Test
    @DisplayName("Deve realizar a transferencia com sucesso")
    void shouldTransferMoneySuccessfully(){
        //Arrange
        User payer = new User("Payer","111","payer@email.com","123",new Wallet(new BigDecimal("100.00")));
        payer.setId(1L);

        User payee = new User("Payee","222","payee@email.com","123",new Wallet(new BigDecimal("0.00")));
        payee.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));

        TransferDTO transferData = new TransferDTO(1L,2L,new BigDecimal("80.00"));

        //Act
        transferService.transfer(transferData);

        //Assert
        assertEquals(new BigDecimal("20.00"), payer.getWallet().getBalance());
        assertEquals(new BigDecimal("80.00"), payee.getWallet().getBalance());

        verify(userRepository, times(1)).save(payer);
        verify(userRepository, times(1)).save(payee);
    }

    @Test
    @DisplayName("Transação nao deve ser completada se o saldo for insuficiente")
    void shouldNotCompleteWithInsufficientBalance(){
        //Arrange
        User payer = new User("Payer","111","payer@email.com","123",new Wallet(new BigDecimal("80.00")));
        payer.setId(1L);

        User payee = new User("Payee","222","payee@email.com","123",new Wallet(new BigDecimal("0.00")));
        payee.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));

        TransferDTO transferData = new TransferDTO(1L,2L,new BigDecimal("100.00"));


        //Act + Assert
        assertThrows(InsufficientBalanceException.class, ()-> transferService.transfer(transferData));

        verify(userRepository, never()).save(any());

        assertEquals(new BigDecimal("80.00"), payer.getWallet().getBalance());
        assertEquals(new BigDecimal("0.00"), payee.getWallet().getBalance());

    }
    @Test
    @DisplayName("Transação nao deve ser completada se o payer e payee tiverem o mesmo CPF")
    void shouldNotCompleteWhenSameCpf(){
        //Arrange
        User payer = new User("Payer","111","payer@email.com","123",new Wallet(new BigDecimal("80.00")));
        payer.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));

        TransferDTO transferData = new TransferDTO(1L,1L,new BigDecimal("50.00"));


        //Act + Assert
        assertThrows(TransferNotAllowedException.class, ()-> transferService.transfer(transferData));

        verify(userRepository, never()).save(any());

        assertEquals(new BigDecimal("80.00"), payer.getWallet().getBalance());
    }
    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro ao salvar")
    void shouldThrowExceptionWhenSaveFails() {
        User payer = new User("Payer", "111", "payer@email.com", "123", new Wallet(new BigDecimal("100.00")));
        payer.setId(1L);

        User payee = new User("Payee", "222", "payee@email.com", "123", new Wallet(new BigDecimal("0.00")));
        payee.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));

        when(userRepository.save(payer)).thenReturn(payer);
        doThrow(new RuntimeException("Erro ao conectar no DB")).when(userRepository).save(payee);

        TransferDTO transferData = new TransferDTO(1L, 2L, new BigDecimal("50.00"));

        assertThrows(RuntimeException.class, () -> transferService.transfer(transferData));

        verify(userRepository, times(1)).save(payer);
        verify(userRepository, times(1)).save(payee);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o pagador não for encontrado")
    void shouldThrowExceptionWhenPayerNotFound(){
        //Arrange
        TransferDTO transferData = new TransferDTO(99L, 2L, new BigDecimal("50.00"));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        //Act + Assert
        assertThrows(UserNotFoundException.class, () -> transferService.transfer(transferData));

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o recebedor não for encontrado")
    void shouldThrowExceptionWhenPayeeNotFound() {
        //Arrange
        User payer = new User("Payer", "111", "payer@email.com", "123", new Wallet(new BigDecimal("100.00")));
        payer.setId(1L);

        TransferDTO transferData = new TransferDTO(1L, 99L, new BigDecimal("50.00"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        //Act + Assert
        assertThrows(UserNotFoundException.class, () -> transferService.transfer(transferData));

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve permitir transferência com valor zero")
    void shouldNotTransferZeroValue() {
        TransferDTO transferData = new TransferDTO(1L, 2L, BigDecimal.ZERO);

        assertThrows(TransferNotAllowedException.class, () -> transferService.transfer(transferData));

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve permitir transferência com valor negativo")
    void shouldNotTransferNegativeValue() {
        TransferDTO transferData = new TransferDTO(1L, 2L, new BigDecimal("-10.00"));

        assertThrows(TransferNotAllowedException.class, () -> transferService.transfer(transferData));

        verify(userRepository, never()).save(any());
    }
}
