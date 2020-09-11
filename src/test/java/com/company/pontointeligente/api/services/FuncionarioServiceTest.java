package com.company.pontointeligente.api.services;

import com.company.pontointeligente.api.entities.Funcionario;
import com.company.pontointeligente.api.repositories.FuncionarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioServiceTest {

    @MockBean
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FuncionarioService funcionarioService;

    @BeforeEach
    public void setUp() {
        BDDMockito.given(this.funcionarioRepository.save(Mockito.any(Funcionario.class)))
            .willReturn(new Funcionario());
        BDDMockito.given(this.funcionarioRepository.findByCpf(Mockito.anyString()))
            .willReturn(new Funcionario());
        BDDMockito.given(this.funcionarioRepository.findByEmail(Mockito.anyString()))
            .willReturn(new Funcionario());
        BDDMockito.given(this.funcionarioRepository.findById(Mockito.anyLong()))
            .willReturn(Optional.of(new Funcionario()));
    }

    @Test
    public void testPersistirFuncionario() {
        Funcionario funcionario = this.funcionarioService.persistir(new Funcionario());

        Assertions.assertNotNull(funcionario);
    }

    @Test
    public void testBuscarPorCpf() {
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorCpf("21383792054");

        Assertions.assertTrue(funcionario.isPresent());
    }

    @Test
    public void testBuscarPorEmail() {
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorEmail("email@email.com.br");

        Assertions.assertTrue(funcionario.isPresent());
    }

    @Test
    public void testBuscarPorId() {
        Optional<Funcionario> funcionario = this.funcionarioService.buscaPorId(1L);

        Assertions.assertTrue(funcionario.isPresent());
    }

}
