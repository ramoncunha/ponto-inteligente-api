package com.company.pontointeligente.api.security.services.impl;

import com.company.pontointeligente.api.security.entities.Usuario;
import com.company.pontointeligente.api.security.repositories.UsuarioRepository;
import com.company.pontointeligente.api.security.services.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return Optional.ofNullable(this.usuarioRepository.findByEmail(email));
    }

}
