package com.meli.bootcamp.integrativeproject.auth.config;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meli.bootcamp.integrativeproject.auth.model.Usuario;
import com.meli.bootcamp.integrativeproject.repositories.AgentRepository;
import com.meli.bootcamp.integrativeproject.repositories.BuyerRepository;
import com.meli.bootcamp.integrativeproject.repositories.SellerRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter{

    private TokenService tokenService;

    private AgentRepository agentRepository;

    private SellerRepository sellerRepository;

    private BuyerRepository buyerRepository;

//    public AutenticacaoViaTokenFilter(TokenService tokenService, AgentRepository agentRepository){
//        this.tokenService = tokenService;
//        this.agentRepository = agentRepository;
//    }


    public AutenticacaoViaTokenFilter(TokenService tokenService, AgentRepository agentRepository, SellerRepository sellerRepository, BuyerRepository buyerRepository) {
        this.tokenService = tokenService;
        this.agentRepository = agentRepository;
        this.sellerRepository = sellerRepository;
        this.buyerRepository = buyerRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //obtem token do cabecalho da requisicao
        String token = extraiToken(request);
        //validar token
        boolean tokenValido = tokenService.tokenValido(token);

        if(tokenValido) {
            //autenticar o token
            realizaAutenticacaoDoTokenNoSpring(token);
        }
        filterChain.doFilter(request, response);
    }

    private void realizaAutenticacaoDoTokenNoSpring(String token) {
        String userName = tokenService.getUsername(token);

        Usuario usuario = this.agentRepository.findByUserName(userName);
        if (usuario == null) {
            usuario = this.sellerRepository.findByUserName(userName);
        }

        if (usuario == null) {
            usuario = this.buyerRepository.findByUserName(userName);
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication); //forçando autenticacao pelo spring
    }

    private String extraiToken(HttpServletRequest request) {
        String token = "";
        String authorization = request.getHeader("Authorization");
        if(authorization==null || authorization.isEmpty() || !authorization.startsWith("Bearer ")) {
            return null;
        }else {
            token = authorization.substring(7, authorization.length());
        }
        return token;
    }

}
