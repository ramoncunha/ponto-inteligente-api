package com.company.pontointeligente.api.services;

import com.company.pontointeligente.api.entities.Empresa;

import java.util.Optional;

public interface EmpresaService {

    /**
     * Retorna uma empresa dado um CNPJ.
     *
     * @param cnpj
     * @return Optional<Empresa>
     */
    Optional<Empresa> buscarPorCnpj(String cnpj);

    /**
     * Cadastra uma nova empresa no banco de dados.
     *
     * @param empresa
     * @return
     */
    Empresa persistir(Empresa empresa);
}
