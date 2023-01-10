package com.ensa.serviceclient.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypePieceIdentite {
    CARTEIDENTITE("carte d'identité"),CERTIFICATMEDICAL("certificat médical")
    ,PASSPORT("Passport"),PERMISDECONDUITE("Permis de Conduite"),AUTRE("autre");
    private  String abrevation;
}
