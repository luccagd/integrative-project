package com.meli.bootcamp.integrativeproject.auth.config;

import com.meli.bootcamp.integrativeproject.auth.model.Usuario;
import com.meli.bootcamp.integrativeproject.repositories.AgentRepository;
import com.meli.bootcamp.integrativeproject.repositories.BuyerRepository;
import com.meli.bootcamp.integrativeproject.repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    AgentRepository agentRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //return new Usuario("lucca", "$2a$12$vxsTNC8Wee0SV1nvu8K/4OwRG/V01oix8eK7FqcpMgf6rLgqFAjI6");
        Usuario usuario = agentRepository.findByUserName(username);
        if (usuario == null) {
            usuario = this.sellerRepository.findByUserName(username);
        }

        if (usuario == null) {
            usuario = this.buyerRepository.findByUserName(username);
        }

        return usuario;


    }

}
