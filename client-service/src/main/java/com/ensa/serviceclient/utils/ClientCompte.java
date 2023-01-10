package com.ensa.serviceclient.utils;

import com.ensa.serviceclient.entities.Client;
import com.ensa.serviceclient.entities.Compte;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientCompte {

    private Client client;
    private Compte compte;
}
