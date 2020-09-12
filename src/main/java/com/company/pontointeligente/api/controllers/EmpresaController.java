package com.company.pontointeligente.api.controllers;

import com.company.pontointeligente.api.dtos.EmpresaDto;
import com.company.pontointeligente.api.entities.Empresa;
import com.company.pontointeligente.api.response.Response;
import com.company.pontointeligente.api.services.EmpresaService;
import com.company.pontointeligente.api.utils.ConversorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {

    private EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping(value = "/cnpj/{cnpj}")
    public ResponseEntity<Response<EmpresaDto>> buscarPorCnpj(@PathVariable("cnpj") String cnpj) {
        log.info("Buscando empresa por CNPJ: {}", cnpj);
        Response<EmpresaDto> response = new Response<>();
        Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cnpj);

        if(!empresa.isPresent()) {
            log.info("Empresa não encontrada para o CNPJ: {}", cnpj);
            response.getErrors().add("Empresa não encontrada para o CNPJ " + cnpj);
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(ConversorDto.converterEmpresaParaEmpresaDto(empresa.get()));
        return ResponseEntity.ok(response);
    }
}
