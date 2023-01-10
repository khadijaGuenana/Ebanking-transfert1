package com.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.java.models.Pointdevente;

@RepositoryRestResource
public interface PointdeventeRepository extends JpaRepository<Pointdevente, Long> {

}
