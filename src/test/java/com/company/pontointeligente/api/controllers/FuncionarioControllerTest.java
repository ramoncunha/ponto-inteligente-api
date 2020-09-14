package com.company.pontointeligente.api.controllers;

import com.company.pontointeligente.api.dtos.FuncionarioDto;
import com.company.pontointeligente.api.entities.Funcionario;
import com.company.pontointeligente.api.enums.PerfilEnum;
import com.company.pontointeligente.api.services.FuncionarioService;
import com.company.pontointeligente.api.utils.PasswordUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FuncionarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FuncionarioService funcionarioService;

    private static final String ALTERAR_FUNCIONARIO_URL = "/api/funcionarios/";
    private static final Long ID_ALTERADO = Long.valueOf(1);
    private static final String NOME_ALTERADO = "Fulano de Tal";
    private static final String EMAIL_ALTERADO = "fulanodetal@email.com";

    @Test
    public void testAlterarFuncionarioFalha() throws Exception {
        BDDMockito.given(this.funcionarioService.buscaPorId(Mockito.anyLong()))
                .willReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.put(ALTERAR_FUNCIONARIO_URL + ID_ALTERADO)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testAlterarFuncionarioSucesso() throws Exception {
        BDDMockito.given(this.funcionarioService.buscaPorId(Mockito.anyLong()))
            .willReturn(Optional.of(this.obterDadosFuncionario()));
        BDDMockito.given(this.funcionarioService.persistir(Mockito.any(Funcionario.class)))
            .willReturn(this.obterDadosFuncionarioEsperado());

        mvc.perform(MockMvcRequestBuilders.put(ALTERAR_FUNCIONARIO_URL + ID_ALTERADO)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(this.obterDadosFuncionarioEsperado())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.id").value(ID_ALTERADO))
            .andExpect(jsonPath("$.data.nome", equalTo(NOME_ALTERADO)))
            .andExpect(jsonPath("$.data.email", equalTo(EMAIL_ALTERADO)));
    }

    public Funcionario obterDadosFuncionario() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(ID_ALTERADO);
        funcionario.setNome("Fulano");
        funcionario.setEmail("fulano@email.com");

        return funcionario;
    }

    public Funcionario obterDadosFuncionarioEsperado() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(ID_ALTERADO);
        funcionario.setNome(NOME_ALTERADO);
        funcionario.setEmail(EMAIL_ALTERADO);

        return funcionario;
    }

}
