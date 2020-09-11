package com.company.pontointeligente.api.services;

import com.company.pontointeligente.api.entities.Funcionario;

import java.util.Optional;

public interface FuncionarioService {

    /**
     * Persiste um funcionário na base de dados.
     *
     * @param funcionario
     * @return
     */
    Funcionario persistir(Funcionario funcionario);

    /**
     * Busca e retorna um funcionário dado um CPF.
     *
     * @param cpf
     * @return
     */
    Optional<Funcionario> buscarPorCpf(String cpf);

    /**
     * Busca e retorna um funcionário dado um email.
     *
     * @param email
     * @return
     */
    Optional<Funcionario> buscarPorEmail(String email);

    /**
     * Busca e retorna um funcionário por ID.
     *
     * @param id
     * @return
     */
    Optional<Funcionario> buscaPorId(Long id);
}
