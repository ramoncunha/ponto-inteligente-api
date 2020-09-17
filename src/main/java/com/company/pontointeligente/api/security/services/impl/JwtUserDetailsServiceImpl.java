package com.company.pontointeligente.api.security.services.impl;

import com.company.pontointeligente.api.security.entities.Usuario;
import com.company.pontointeligente.api.security.JwtUserFactory;
import com.company.pontointeligente.api.security.services.UsuarioService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private UsuarioService usuarioService;

    public JwtUserDetailsServiceImpl(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> funcionario = this.usuarioService.buscarPorEmail(username);
        if(funcionario.isPresent()) {
            return JwtUserFactory.create(funcionario.get());
        }
        throw new UsernameNotFoundException("Email não encontrado");
    }
}
