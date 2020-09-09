package com.company.pontointeligente.api.repositories;

import com.company.pontointeligente.api.entities.Empresa;
import com.company.pontointeligente.api.entities.Funcionario;
import com.company.pontointeligente.api.enums.PerfilEnum;
import com.company.pontointeligente.api.util.EmpresaCreator;
import com.company.pontointeligente.api.util.FuncionarioCreator;
import com.company.pontointeligente.api.utils.PasswordUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioRepositoryTest {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    private static final String EMAIL = "email@email.com";
    private static final String CPF = "21383792054";

    @BeforeEach
    public void setUp() {
        Empresa empresa = this.empresaRepository.save(EmpresaCreator.obterDadosEmpresa());
        this.funcionarioRepository.save(FuncionarioCreator.obterDadosFuncionario(empresa));
    }

    @AfterEach
    public final void tearDown() {
        this.empresaRepository.deleteAll();
    }

    @Test
    public void testBuscarFuncionarioPorEmail() {
        Funcionario funcionario = this.funcionarioRepository.findByEmail(EMAIL);

        Assertions.assertEquals(EMAIL, funcionario.getEmail());
    }

    @Test
    public void testBuscarFuncionarioPorCpf() {
        Funcionario funcionario = this.funcionarioRepository.findByCpf(CPF);

        Assertions.assertEquals(CPF, funcionario.getCpf());
    }

    @Test
    public void testBuscarFuncionarioPorEmailECpf() {
        Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(CPF, EMAIL);

        Assertions.assertNotNull(funcionario);
    }

    @Test
    public void testBuscarFuncionarioPorEmailOuCpfParaEmailInvalido() {
        Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(CPF, "email@invalid.com.br");

        Assertions.assertNotNull(funcionario);
    }

    @Test
    public void testBuscarPorFuncionarioPorEmailECpfParaCpfInvalido() {
        Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail("12345678901", EMAIL);

        Assertions.assertNotNull(funcionario);
    }
}
