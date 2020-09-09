package com.company.pontointeligente.api.util;

import com.company.pontointeligente.api.entities.Empresa;
import com.company.pontointeligente.api.entities.Funcionario;
import com.company.pontointeligente.api.enums.PerfilEnum;
import com.company.pontointeligente.api.utils.PasswordUtils;

public class FuncionarioCreator {

    private static final String EMAIL = "email@email.com";
    private static final String CPF = "21383792054";

    public static Funcionario obterDadosFuncionario(Empresa empresa) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Ramon");
        funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
        funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
        funcionario.setCpf(CPF);
        funcionario.setEmail(EMAIL);
        funcionario.setEmpresa(empresa);
        return funcionario;
    }

}
