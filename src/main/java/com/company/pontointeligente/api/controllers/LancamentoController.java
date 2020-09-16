package com.company.pontointeligente.api.controllers;

import com.company.pontointeligente.api.dtos.LancamentoDto;
import com.company.pontointeligente.api.entities.Funcionario;
import com.company.pontointeligente.api.entities.Lancamento;
import com.company.pontointeligente.api.enums.TipoEnum;
import com.company.pontointeligente.api.response.Response;
import com.company.pontointeligente.api.services.FuncionarioService;
import com.company.pontointeligente.api.services.LancamentoService;
import com.company.pontointeligente.api.utils.ConversorDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin(origins = "*")
public class LancamentoController {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final LancamentoService lancamentoService;
    private final FuncionarioService funcionarioService;
    @Value("${paginacao.qtd_por_pagina}")
    private int qtdPorPagina;

    public LancamentoController(LancamentoService lancamentoService, FuncionarioService funcionarioService) {
        this.lancamentoService = lancamentoService;
        this.funcionarioService = funcionarioService;
    }

    @GetMapping(value = "/funcionario/{funcionarioId}")
    public ResponseEntity<Response<Page<LancamentoDto>>> listarPorFuncionarioId(
            @PathVariable("funcionarioId") Long funcionarioId,
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "id") String ord,
            @RequestParam(value = "dir", defaultValue = "DESC") String dir) {
        log.info("Buscando lançamentos por ID do funcionário: {}, página: {}", funcionarioId, pag);
        Response<Page<LancamentoDto>> response = new Response<>();

        PageRequest pageRequest = PageRequest.of(pag, this.qtdPorPagina, Sort.Direction.valueOf(dir), ord);
        Page<Lancamento> lancamentos = this.lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest);
        Page<LancamentoDto> lancamentosDtos = lancamentos.map(lancamento -> ConversorDto.converterLancamentoParaLancamentoDto(lancamento));

        response.setData(lancamentosDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Response<LancamentoDto>> listarPorId(@PathVariable("id") Long id) {
        log.info("Buscando lançamento por ID: {}", id);
        Response<LancamentoDto> response = new Response<>();
        Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

        if(!lancamento.isPresent()) {
            log.error("Lançamento não encontrado para o id {}", id);
            response.getErrors().add("Lançamento não encontrado para o id " + id);
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(ConversorDto.converterLancamentoParaLancamentoDto(lancamento.get()));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Response<LancamentoDto>> adicionar(@Valid @RequestBody LancamentoDto lancamentoDto,
                                                             BindingResult result) throws ParseException {
        log.info("Adicionar lançamento: {}", lancamentoDto.toString());
        Response<LancamentoDto> response = new Response<>();
        this.validarFuncionario(lancamentoDto, result);
        this.validarLancamento(lancamentoDto, result);
        Lancamento lancamento = ConversorDto.converterLancamentoDtoParaPersistirLancamento(lancamentoDto);

        if(result.hasErrors()) {
            log.error("Erro validando lançamento: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        lancamento = this.lancamentoService.persistir(lancamento);
        response.setData(ConversorDto.converterLancamentoParaLancamentoDto(lancamento));
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Response<LancamentoDto>> atualizar(@PathVariable Long id,
                                                             @Valid @RequestBody LancamentoDto lancamentoDto,
                                                             BindingResult result) throws ParseException {
        log.info("Atualizar lançamento: {}", lancamentoDto.toString());
        Response<LancamentoDto> response = new Response<>();
        this.validarFuncionario(lancamentoDto, result);
        this.validarLancamento(lancamentoDto, result);
        lancamentoDto.setId(Optional.of(id));
        Optional<Lancamento> lancamentoAntigo = this.lancamentoService.buscarPorId(id);
        Lancamento lancamentoNovo = ConversorDto.converterLancamentoDtoParaAtualizarLancamento(lancamentoDto, lancamentoAntigo);

        if(result.hasErrors()) {
            log.error("Erro validando lançamentos: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        lancamentoNovo = this.lancamentoService.persistir(lancamentoNovo);
        response.setData(ConversorDto.converterLancamentoParaLancamentoDto(lancamentoNovo));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response<String>> remover(@PathVariable Long id) {
        log.info("Removendo lançamento: {}", id);
        Response<String> response = new Response<>();
        Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

        if(!lancamento.isPresent()) {
            log.error("Erro ao remover lançamento ID: {}", id);
            response.getErrors().add("Erro ao remover. Registro não encontrado para o id " + id);
            return ResponseEntity.badRequest().body(response);
        }

        this.lancamentoService.remover(id);
        return ResponseEntity.ok(new Response<String>());
    }

    private void validarLancamento(LancamentoDto lancamentoDto, BindingResult result) {
        if(!EnumUtils.isValidEnum(TipoEnum.class, lancamentoDto.getTipo())) {
            result.addError(new ObjectError("tipo", "Tipo inválido."));
        }
    }

    private void validarFuncionario(LancamentoDto lancamentoDto, BindingResult result) {
        if(lancamentoDto.getFuncionarioId() == null) {
            result.addError(new ObjectError("funcionario", "Funcionário não informado."));
            return;
        }

        log.info("Validando funcionário id {}", lancamentoDto.getFuncionarioId());
        Optional<Funcionario> funcionario = this.funcionarioService.buscaPorId(lancamentoDto.getFuncionarioId());
        if(!funcionario.isPresent()) {
            result.addError(new ObjectError("funcionario", "Funcionário não encontrado. ID Inexistente."));
        }
    }
}
