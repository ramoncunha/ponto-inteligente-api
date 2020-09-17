package com.company.pontointeligente.api.services;

import com.company.pontointeligente.api.entities.Usuario;

import java.util.Optional;

public interface UsuarioService {

    Optional<Usuario> buscarPorEmail(String email);

}
