package com.meli.bootcamp.integrativeproject.auth.controller;

import com.meli.bootcamp.integrativeproject.auth.config.TokenService;
import com.meli.bootcamp.integrativeproject.auth.model.Usuario;
import com.meli.bootcamp.integrativeproject.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenDTO> authenticate(@RequestBody Usuario usuario) {
        UsernamePasswordAuthenticationToken dadosLogin = new UsernamePasswordAuthenticationToken(usuario.getUsername(), usuario.getPassword());
        Authentication authentication = manager.authenticate(dadosLogin);

        String token = tokenService.geraToken(authentication);
        return ResponseEntity.ok(new TokenDTO(token, "Bearer"));
    }
}
