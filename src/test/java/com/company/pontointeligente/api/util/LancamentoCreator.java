package com.company.pontointeligente.api.util;

import com.company.pontointeligente.api.entities.Funcionario;
import com.company.pontointeligente.api.entities.Lancamento;
import com.company.pontointeligente.api.enums.TipoEnum;

import java.util.Date;

public class LancamentoCreator {

    public static Lancamento obterDadosLancamento(Funcionario funcionario) {
        Lancamento lancamento = new Lancamento();
        lancamento.setData(new Date());
        lancamento.setTipo(TipoEnum.INICIO_ALMOCO);
        lancamento.setFuncionario(funcionario);
        return lancamento;
    }

    public static Lancamento obterDadosLancamentoEsperado() {
        Lancamento lancamento = new Lancamento();
        lancamento.setId(1L);
        lancamento.setData(new Date());
        lancamento.setTipo(TipoEnum.INICIO_TRABALHO);
        lancamento.setFuncionario(new Funcionario());
        lancamento.getFuncionario().setId(1L);
        return lancamento;
    }

}
