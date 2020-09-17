package com.company.pontointeligente.api.security;

import com.company.pontointeligente.api.security.entities.Usuario;
import com.company.pontointeligente.api.enums.PerfilEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(Usuario usuario) {
        return new JwtUser(usuario.getId(), usuario.getEmail(),
                usuario.getSenha(),
                mapToGrantedAuthorities(usuario.getPerfil()));
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(PerfilEnum perfilEnum) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(perfilEnum.toString()));
        return authorities;
    }
}
