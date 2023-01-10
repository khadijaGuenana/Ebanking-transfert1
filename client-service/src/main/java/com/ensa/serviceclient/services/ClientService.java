package com.ensa.serviceclient.services;

import com.ensa.serviceclient.entities.*;
import com.ensa.serviceclient.repositories.BeneficiaireRepo;
import com.ensa.serviceclient.repositories.ClientRepo;
import com.ensa.serviceclient.utils.ClientCompte;
import com.ensa.serviceclient.utils.Login;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
@Data
public class ClientService {
    final private RestTemplate restTemplate;
    @Autowired
    ClientRepo clientRepository;
    @Autowired
    BeneficiaireRepo beneficiaireRepo;
    public List<Transfer> getHistorique(Long id) {
        Transfer[] transfers = restTemplate.getForObject("http://TRANSFER-SERVICE/transferservice/transfer/"+id, Transfer[].class);
        return Arrays.asList(transfers);
    }
    public void emissionTransfert(Transfer transferEmis) {
        restTemplate.postForObject("http://TRANSFER-SERVICE/transferservice/transfer/dabit/20000",transferEmis,Transfer.class);
    }
    public Client addClient(Client client) {
        Client cl = clientRepository.findByPhone(client.getPhone());
        if(cl != null)
            return cl;
        return clientRepository.save(client);
    }
    public Client getClient(Long id){
        return clientRepository.findById(id).get();
    }

    public ResponseEntity<?> login(Login login){
        Client client = clientRepository.findByPhone(login.getPhone());
        Compte compte =  restTemplate.getForObject("http://AGENT-SERVICE/agent/compte/"+login.getPin(),Compte.class);
        System.err.println(compte.getNumero());
        if(client != null && compte != null && compte.getClient()==client.getId()) {
            ClientCompte clientCompte = new ClientCompte(client,compte);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(clientCompte);
        }else{
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("les informations invalides !!");
        }
    }


    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    public List<Client> deleteClient(Long id) {
        clientRepository.deleteById(id);
        return clientRepository.findAll();
    }

    public Beneficiaire addBenef(Beneficiaire benef) {
        Beneficiaire cl = beneficiaireRepo.findByPieceIdentite(benef.getPieceIdentite());
        if(cl != null)
            return cl;
        return beneficiaireRepo.save(benef);
    }

    public List<Beneficiaire> getBenefs() {
        return beneficiaireRepo.findAll();
    }

    public List<Beneficiaire> addBenefs(List<Beneficiaire> benefs) {
        List<Beneficiaire> beneficiaires = new ArrayList<>();
        for(Beneficiaire benef:benefs){
            Beneficiaire cl = beneficiaireRepo.findByNumIdentite(benef.getNumIdentite());
            if(cl != null) {
                beneficiaires.add(cl);
            }
            else {
                benef.setPieceIdentite(TypePieceIdentite.CARTEIDENTITE);

                beneficiaires.add(beneficiaireRepo.save(benef)) ;
            }

        }
        return beneficiaires;

    }
}

