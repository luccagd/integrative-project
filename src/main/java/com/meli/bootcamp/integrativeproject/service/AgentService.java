package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.entity.Agent;
import com.meli.bootcamp.integrativeproject.repositories.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {

    private AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public List<Agent> findAll(){
        return agentRepository.findAll();
    }

    public Agent findById(Long id){
       return agentRepository.findById(id).orElseThrow(() -> new RuntimeException("NOT FOUND"));
    }

    public Agent save(Agent agent){
        return agentRepository.save(agent);
    }

    public void deleteById(Long id){
       agentRepository.deleteById(id);
    }

}
