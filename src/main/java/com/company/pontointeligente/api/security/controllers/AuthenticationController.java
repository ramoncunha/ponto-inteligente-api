package com.company.pontointeligente.api.security.controllers;

import com.company.pontointeligente.api.response.Response;
import com.company.pontointeligente.api.security.dtos.JwtAuthenticationDto;
import com.company.pontointeligente.api.security.dtos.TokenDto;
import com.company.pontointeligente.api.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private String TOKEN_HEADER = "Authorization";
    private String BEARER_PREFIX = "Bearer ";

    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private UserDetailsService userDetailsService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public ResponseEntity<Response<TokenDto>> gerarTokenJwt(@Valid @RequestBody JwtAuthenticationDto authenticationDto,
                                                            BindingResult result) {
        Response<TokenDto> response = new Response<>();

        if(result.hasErrors()) {
            log.error("Erro validando token: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        log.info("Gerando token para o email: {}", authenticationDto.getEmail());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationDto.getEmail(), authenticationDto.getSenha()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDto.getEmail());
        String token = jwtTokenUtil.obterToken(userDetails);
        response.setData(new TokenDto(token));

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<Response<TokenDto>> gerarRefreshTokenJwt(HttpServletRequest request) {
        log.info("Gerando refresh token JWT.");
        Response<TokenDto> response = new Response<>();

        Optional<String> token = Optional.ofNullable(request.getHeader(TOKEN_HEADER));

        if(token.isPresent() && token.get().startsWith(BEARER_PREFIX)) {
            token = Optional.of(token.get().substring(7));
        }

        if(!token.isPresent()) {
            response.getErrors().add("Token não informado");
        } else if (!jwtTokenUtil.tokenValido(token.get())) {
            response.getErrors().add("Token inválido ou expirado.");
        }

        if(!response.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(response);
        }

        String refreshedToken = jwtTokenUtil.refreshToken(token.get());
        response.setData(new TokenDto(refreshedToken));

        return ResponseEntity.ok(response);
    }
}
