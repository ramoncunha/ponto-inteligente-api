package com.company.pontointeligente.api.repositories;

import com.company.pontointeligente.api.entities.Empresa;
import com.company.pontointeligente.api.entities.Funcionario;
import com.company.pontointeligente.api.entities.Lancamento;
import com.company.pontointeligente.api.util.EmpresaCreator;
import com.company.pontointeligente.api.util.FuncionarioCreator;
import com.company.pontointeligente.api.util.LancamentoCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    private Long funcionarioId;

    @BeforeEach
    public void setUp() {
        Empresa empresa = this.empresaRepository.save(EmpresaCreator.obterDadosEmpresa());
        Funcionario funcionario = this.funcionarioRepository.save(FuncionarioCreator.obterDadosFuncionario(empresa));
        this.funcionarioId = funcionario.getId();

        this.lancamentoRepository.save(LancamentoCreator.obterDadosLancamento(funcionario));
        this.lancamentoRepository.save(LancamentoCreator.obterDadosLancamento(funcionario));
    }

    @AfterEach
    public void tearDown() {
        this.empresaRepository.deleteAll();
    }

    @Test
    public void testBuscarLancamentoPorFuncionarioId() {
        List<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId);

        Assertions.assertEquals(2, lancamentos.size());
    }

    @Test
    public void testBuscarLancamentosPorFuncionarioIdPaginado() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);

        Assertions.assertEquals(2, lancamentos.getTotalElements());
    }

}
