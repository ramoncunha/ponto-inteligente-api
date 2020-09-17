package com.company.pontointeligente.api.security.services;

import com.company.pontointeligente.api.security.entities.Usuario;

import java.util.Optional;

public interface UsuarioService {

    Optional<Usuario> buscarPorEmail(String email);

}
