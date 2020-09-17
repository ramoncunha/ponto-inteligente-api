package com.company.pontointeligente.api.security.services;

import com.company.pontointeligente.api.entities.Funcionario;
import com.company.pontointeligente.api.security.JwtUserFactory;
import com.company.pontointeligente.api.services.FuncionarioService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private FuncionarioService funcionarioService;

    public JwtUserDetailsServiceImpl(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorEmail(username);
        if(funcionario.isPresent()) {
            return JwtUserFactory.create(funcionario.get());
        }
        throw new UsernameNotFoundException("Email não encontrado");
    }
}
