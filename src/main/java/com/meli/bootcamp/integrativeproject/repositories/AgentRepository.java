package com.meli.bootcamp.integrativeproject.repositories;

import com.meli.bootcamp.integrativeproject.auth.model.Usuario;
import com.meli.bootcamp.integrativeproject.entity.Agent;
import com.meli.bootcamp.integrativeproject.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends JpaRepository<Agent,Long> {

    Agent findByUsuario(String usuario);
}
