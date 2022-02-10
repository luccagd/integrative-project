package com.meli.bootcamp.integrativeproject.auth.config;

import com.meli.bootcamp.integrativeproject.repositories.AgentRepository;
import com.meli.bootcamp.integrativeproject.repositories.BuyerRepository;
import com.meli.bootcamp.integrativeproject.repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationService service;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                .antMatchers( "/fresh-products").hasAnyAuthority("AGENT")
                .antMatchers( "/fresh-products/list").hasAnyAuthority("AGENT")
                .antMatchers( "/fresh-products/list/byName").hasAnyAuthority("AGENT")
                .antMatchers( "/fresh-products/list/byCategory").hasAnyAuthority("AGENT")
                .antMatchers( "/fresh-products/inboundorder").hasAnyAuthority("AGENT")
                .antMatchers( "/fresh-products/warehouse").hasAnyAuthority("AGENT")
                .antMatchers( "/fresh-products/due-date/").hasAnyAuthority("AGENT")
                .antMatchers( HttpMethod.GET, "/fresh-products/orders").hasAnyAuthority("AGENT")
                .antMatchers( HttpMethod.POST, "/fresh-products/orders").hasAnyAuthority("BUYER")
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and().headers().frameOptions().disable()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, agentRepository, sellerRepository, buyerRepository), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        auth.userDetailsService(service).passwordEncoder(encoder);
    }
}
