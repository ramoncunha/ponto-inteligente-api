package com.company.pontointeligente.api.controllers;

import com.company.pontointeligente.api.dtos.CadastroPJDto;
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

@Slf4j
@RestController
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin(origins = "*")
public class CadastroPJController {

    private final FuncionarioService funcionarioService;

    private final EmpresaService empresaService;

    public CadastroPJController(FuncionarioService funcionarioService, EmpresaService empresaService) {
        this.funcionarioService = funcionarioService;
        this.empresaService = empresaService;
    }

    @PostMapping
    public ResponseEntity<Response<CadastroPJDto>> cadastrar(@Valid @RequestBody CadastroPJDto cadastroPJDto,
                                                             BindingResult result) {
        log.info("Cadastrando PJ: {}", cadastroPJDto.toString());
        Response<CadastroPJDto> response = new Response<>();

        this.validarDadosExistentes(cadastroPJDto, result);
        Empresa empresa = ConversorDto.converterPJDtoParaEmpresa(cadastroPJDto);
        Funcionario funcionario = ConversorDto.converterPJDtoParaFuncionario(cadastroPJDto);

        if(result.hasErrors()) {
            log.error("Erro validando dados de cadastro PJ: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        this.empresaService.persistir(empresa);
        funcionario.setEmpresa(empresa);
        this.funcionarioService.persistir(funcionario);

        response.setData(ConversorDto.converterFuncionarioParaPJDto(funcionario));
        return ResponseEntity.ok(response);
    }

    private void validarDadosExistentes(CadastroPJDto cadastroPJDto, BindingResult result) {
        this.empresaService.buscarPorCnpj(cadastroPJDto.getCnpj())
            .ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existente.")));

        this.funcionarioService.buscarPorCpf(cadastroPJDto.getCpf())
            .ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente.")));

        this.funcionarioService.buscarPorEmail(cadastroPJDto.getEmail())
            .ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente.")));
    }
}
