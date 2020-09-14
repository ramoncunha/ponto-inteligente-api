package com.company.pontointeligente.api.controllers;

import com.company.pontointeligente.api.dtos.LancamentoDto;
import com.company.pontointeligente.api.entities.Lancamento;
import com.company.pontointeligente.api.response.Response;
import com.company.pontointeligente.api.services.FuncionarioService;
import com.company.pontointeligente.api.services.LancamentoService;
import com.company.pontointeligente.api.utils.ConversorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

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


}
