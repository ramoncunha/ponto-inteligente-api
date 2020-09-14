package com.company.pontointeligente.api.utils;

import com.company.pontointeligente.api.dtos.*;
import com.company.pontointeligente.api.entities.Empresa;
import com.company.pontointeligente.api.entities.Funcionario;
import com.company.pontointeligente.api.entities.Lancamento;
import com.company.pontointeligente.api.enums.PerfilEnum;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Optional;

public abstract class ConversorDto {

    public static Funcionario converterPJDtoParaFuncionario(CadastroPJDto cadastroPJDto) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(cadastroPJDto.getNome());
        funcionario.setEmail(cadastroPJDto.getEmail());
        funcionario.setCpf(cadastroPJDto.getCpf());
        funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
        funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPJDto.getSenha()));

        return funcionario;
    }

    public static Empresa converterPJDtoParaEmpresa(CadastroPJDto cadastroPJDto) {
        Empresa empresa = new Empresa();
        empresa.setCnpj(cadastroPJDto.getCnpj());
        empresa.setRazaoSocial(cadastroPJDto.getRazaoSocial());

        return empresa;
    }

    public static CadastroPJDto converterFuncionarioParaPJDto(Funcionario funcionario) {
        CadastroPJDto cadastroPJDto = new CadastroPJDto();
        cadastroPJDto.setId(funcionario.getId());
        cadastroPJDto.setNome(funcionario.getNome());
        cadastroPJDto.setEmail(funcionario.getEmail());
        cadastroPJDto.setCpf(funcionario.getCpf());
        cadastroPJDto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
        cadastroPJDto.setCnpj(funcionario.getEmpresa().getCnpj());

        return cadastroPJDto;
    }

    public static Funcionario converterPFDtoParaFuncionario(CadastroPFDto cadastroPFDto) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(cadastroPFDto.getNome());
        funcionario.setEmail(cadastroPFDto.getEmail());
        funcionario.setCpf(cadastroPFDto.getCpf());
        funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
        funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPFDto.getSenha()));
        cadastroPFDto.getQtdHorasAlmoco()
            .ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
        cadastroPFDto.getQtdHorasTrabalhoDia()
            .ifPresent(qtdHorasTrabalhoDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhoDia)));
        cadastroPFDto.getValorHora()
            .ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

        return funcionario;
    }

    public static CadastroPFDto converterFuncionarioParaPFDto(Funcionario funcionario) {
        CadastroPFDto cadastroPFDto = new CadastroPFDto();
        cadastroPFDto.setId(funcionario.getId());
        cadastroPFDto.setNome(funcionario.getNome());
        cadastroPFDto.setEmail(funcionario.getEmail());
        cadastroPFDto.setCpf(funcionario.getCpf());
        cadastroPFDto.setCnpj(funcionario.getEmpresa().getCnpj());
        funcionario.getQtdHorasAlmocoOpt()
            .ifPresent(qtdHorasAlmoco -> cadastroPFDto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
        funcionario.getQtdHorasTrabalhoDiaOpt()
            .ifPresent(qtdHorasTrabalhoDia -> cadastroPFDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabalhoDia))));
        funcionario.getValorHoraOpt()
            .ifPresent(valorHora -> cadastroPFDto.setValorHora(Optional.of(valorHora.toString())));

        return cadastroPFDto;
    }

    public static EmpresaDto converterEmpresaParaEmpresaDto(Empresa empresa) {
        EmpresaDto empresaDto = new EmpresaDto();
        empresaDto.setId(empresa.getId());
        empresaDto.setCnpj(empresa.getCnpj());
        empresaDto.setRazaoSocial(empresa.getRazaoSocial());

        return empresaDto;
    }

    public static FuncionarioDto converterFuncionarioParaFuncionarioDto(Funcionario funcionario) {
        FuncionarioDto funcionarioDto = new FuncionarioDto();
        funcionarioDto.setId(funcionario.getId());
        funcionarioDto.setEmail(funcionario.getEmail());
        funcionarioDto.setNome(funcionario.getNome());
        funcionario.getQtdHorasAlmocoOpt()
            .ifPresent(qtdHorasAlmoco -> funcionarioDto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
        funcionario.getQtdHorasTrabalhoDiaOpt()
            .ifPresent(qtdHorasTrabalhoDia -> funcionarioDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabalhoDia))));
        funcionario.getValorHoraOpt()
            .ifPresent(valorHora -> funcionarioDto.setValorHora(Optional.of(valorHora.toString())));

        return funcionarioDto;
    }

    public static LancamentoDto converterLancamentoParaLancamentoDto(Lancamento lancamento) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        LancamentoDto lancamentoDto = new LancamentoDto();
        lancamentoDto.setId(Optional.of(lancamento.getId()));
        lancamentoDto.setData(dateFormat.format(lancamento.getData()));
        lancamentoDto.setTipo(lancamento.getTipo().toString());
        lancamentoDto.setDescricao(lancamento.getDescricao());
        lancamentoDto.setLocalizacao(lancamento.getLocalizacao());
        lancamentoDto.setFuncionarioId(lancamento.getId());

        return lancamentoDto;
    }
}
