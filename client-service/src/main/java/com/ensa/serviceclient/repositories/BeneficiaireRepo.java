package com.ensa.serviceclient.repositories;

import com.ensa.serviceclient.entities.Beneficiaire;
import com.ensa.serviceclient.entities.TypePieceIdentite;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiaireRepo extends JpaRepository<Beneficiaire,Long> {

    public Beneficiaire findByPieceIdentite(TypePieceIdentite pieceIdentite);
    public Beneficiaire findByNumIdentite(String numIdentite);
}
