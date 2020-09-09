package com.company.pontointeligente.api.repositories;

import com.company.pontointeligente.api.entities.Empresa;
import com.company.pontointeligente.api.util.EmpresaCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class EmpresaRepositoryTest {

    @Autowired
    private EmpresaRepository empresaRepository;
    private static final String CNPJ = "51463645000100";

    @BeforeEach
    public void setUp() {
        Empresa empresa = EmpresaCreator.obterDadosEmpresa();
        this.empresaRepository.save(empresa);
    }

    @AfterEach
    public final void tearDown() {
        this.empresaRepository.deleteAll();
    }

    @Test
    public void testBuscarPorCnpj() {
        Empresa empresa = this.empresaRepository.findByCnpj(CNPJ);
        Assertions.assertEquals(CNPJ, empresa.getCnpj());
    }

}
