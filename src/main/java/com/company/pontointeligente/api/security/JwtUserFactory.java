package com.company.pontointeligente.api.security;

import com.company.pontointeligente.api.entities.Funcionario;
import com.company.pontointeligente.api.enums.PerfilEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(Funcionario funcionario) {
        return new JwtUser(funcionario.getId(), funcionario.getEmail(),
                funcionario.getSenha(),
                mapToGrantedAuthorities(funcionario.getPerfil()));
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(PerfilEnum perfilEnum) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(perfilEnum.toString()));
        return authorities;
    }
}
