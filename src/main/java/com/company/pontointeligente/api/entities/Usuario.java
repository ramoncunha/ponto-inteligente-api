package com.company.pontointeligente.api.entities;

import com.company.pontointeligente.api.enums.PerfilEnum;

import javax.persistence.*;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String senha;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PerfilEnum perfil;

}
