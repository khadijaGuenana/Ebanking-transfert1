package com.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.java.models.Agent;

@RepositoryRestResource
public interface AgentRepository extends JpaRepository<Agent, Long>{

    public Agent findAgentById(Long id);
    public Agent findAgentByUsernameAndPassword(String username,String password);

}
