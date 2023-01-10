package com.ensa.serviceclient.entities;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data @NoArgsConstructor @AllArgsConstructor @ToString

public class Compte {
    private Long id ;
    private int numero ;
    private String pin;
    private double solde ;
    private Long client;

}
