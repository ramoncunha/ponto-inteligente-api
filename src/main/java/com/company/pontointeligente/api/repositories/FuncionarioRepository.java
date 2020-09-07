package com.company.pontointeligente.api.repositories;

import com.company.pontointeligente.api.entities.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Funcionario findByCpf(String cpf);
    Funcionario findByEmail(String email);
    Funcionario findByCpfOrEmail(String cpf, String email);

}
