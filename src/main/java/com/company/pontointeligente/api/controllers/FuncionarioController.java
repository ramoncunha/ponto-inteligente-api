package com.company.pontointeligente.api.controllers;

import com.company.pontointeligente.api.dtos.FuncionarioDto;
import com.company.pontointeligente.api.entities.Funcionario;
import com.company.pontointeligente.api.response.Response;
import com.company.pontointeligente.api.services.FuncionarioService;
import com.company.pontointeligente.api.utils.ConversorDto;
import com.company.pontointeligente.api.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

    private FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Response<FuncionarioDto>> atualizar(@PathVariable("id") Long id,
                                                              @Valid @RequestBody FuncionarioDto funcionarioDto,
                                                              BindingResult result) {
        log.info("Atualizando funcionário: {}", funcionarioDto);
        Response<FuncionarioDto> response = new Response<>();

        Optional<Funcionario> funcionario = this.funcionarioService.buscaPorId(id);
        if(!funcionario.isPresent())
            result.addError(new ObjectError("funcionario", "Funcionário não encontrado."));

        this.atualizarDadosFuncionario(funcionario.get(), funcionarioDto, result);

        if(result.hasErrors()) {
            log.error("Erro validando funcionário: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        this.funcionarioService.persistir(funcionario.get());
        response.setData(ConversorDto.converterFuncionarioParaFuncionarioDto(funcionario.get()));

        return ResponseEntity.ok(response);
    }

    public void atualizarDadosFuncionario(Funcionario funcionario, FuncionarioDto funcionarioDto, BindingResult result) {
        funcionario.setNome(funcionarioDto.getNome());
        if(!funcionario.getEmail().equals(funcionarioDto.getEmail())) {
            this.funcionarioService.buscarPorEmail(funcionarioDto.getEmail())
                .ifPresent(func -> result.addError(new ObjectError("email", "Email já existente.")));
            funcionario.setEmail(funcionarioDto.getEmail());
        }

//        funcionario.setQtdHorasAlmoco(null);
        funcionarioDto.getQtdHorasAlmoco()
            .ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));

//        funcionario.setQtdHorasTrabalhoDia(null);
        funcionarioDto.getQtdHorasTrabalhoDia()
            .ifPresent(qtdHorasTrabalhoDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhoDia)));

//        funcionario.setValorHora(null);
        funcionarioDto.getValorHora()
            .ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

        if(funcionarioDto.getSenha().isPresent()) {
            funcionario.setSenha(PasswordUtils.gerarBCrypt(funcionarioDto.getSenha().get()));
        }
    }
}
