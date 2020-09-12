package com.company.pontointeligente.api.controllers;

import com.company.pontointeligente.api.dtos.CadastroPFDto;
import com.company.pontointeligente.api.entities.Empresa;
import com.company.pontointeligente.api.entities.Funcionario;
import com.company.pontointeligente.api.response.Response;
import com.company.pontointeligente.api.services.EmpresaService;
import com.company.pontointeligente.api.services.FuncionarioService;
import com.company.pontointeligente.api.utils.ConversorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins = "*")
public class CadastroPFController {

    private final EmpresaService empresaService;

    private final FuncionarioService funcionarioService;

    public CadastroPFController(EmpresaService empresaService, FuncionarioService funcionarioService) {
        this.empresaService = empresaService;
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    public ResponseEntity<Response<CadastroPFDto>> cadastrar(@Valid @RequestBody CadastroPFDto cadastroPFDto,
                                                             BindingResult result) {
        log.info("Cadastrando PF: {}", cadastroPFDto.toString());
        Response<CadastroPFDto> response = new Response<>();

        this.validarDadosExistentes(cadastroPFDto, result);
        Funcionario funcionario = ConversorDto.converterPFDtoParaFuncionario(cadastroPFDto);

        if(result.hasErrors()){
            log.error("Erro validando dados de cadastro PF: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
        empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
        this.funcionarioService.persistir(funcionario);

        response.setData(ConversorDto.converterFuncionarioParaPFDto(funcionario));
        return ResponseEntity.ok(response);
    }

    private void validarDadosExistentes(CadastroPFDto cadastroPFDto, BindingResult result) {
        Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
        if(!empresa.isPresent())
            result.addError(new ObjectError("empresa", "Empresa não cadastrada."));

        this.funcionarioService.buscarPorCpf(cadastroPFDto.getCpf())
            .ifPresent(funcionario -> result.addError(new ObjectError("funcionario", "CPF já existente")));

        this.funcionarioService.buscarPorEmail(cadastroPFDto.getEmail())
            .ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente.")));
    }
}
