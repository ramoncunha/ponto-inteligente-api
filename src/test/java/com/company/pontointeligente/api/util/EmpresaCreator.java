package com.company.pontointeligente.api.util;

import com.company.pontointeligente.api.entities.Empresa;

public class EmpresaCreator {

    private static final String CNPJ = "51463645000100";

    public static Empresa obterDadosEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial("Empresa de exemplo");
        empresa.setCnpj("51463645000100");
        return empresa;
    }
}
