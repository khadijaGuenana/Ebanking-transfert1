package com.ensa.serviceclient.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * le client beneficiaire.
 * identifier par sa piece d'identite.
 * le boolean blacklister indique si le beneficiaire est dans une black liste.
 * */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Beneficiaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "beneficiaire_id")
    private Long id;
    @Enumerated(EnumType.STRING)

    private TypePieceIdentite pieceIdentite;
    private String numIdentite;
    private String gsm;
    private boolean blackLister;

}
