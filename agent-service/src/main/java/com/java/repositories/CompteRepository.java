package com.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.java.models.Compte;

@RepositoryRestResource
public interface CompteRepository extends JpaRepository<Compte, Long> {
    public Compte findByPin(String pin);
    public Compte findCompteByClient(Long idClient);
}
