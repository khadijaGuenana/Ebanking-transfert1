package org.sid.transferservice.service;
import org.json.JSONObject;
import org.sid.transferservice.entities.Client;
import org.sid.transferservice.entities.Compte;
import org.sid.transferservice.entities.Transfer;
import org.sid.transferservice.entities.TransferWithClients;
import org.sid.transferservice.enums.EmissionType;
import org.sid.transferservice.enums.ModeCost;
import org.sid.transferservice.enums.Status;
import org.sid.transferservice.repositories.TransferRepository;
import org.sid.transferservice.utils.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransferService {
    @Autowired
    private RestTemplate restTemplate;
    private TransferRepository transferRepository;

    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public ResponseEntity<?> transferEspeceAgent(Transfer transfer, double soldAgent) {
        List<Transfer> transfers = transferRepository.findByClientSrc(transfer.getClientSrc());
        double somme_mont = 0;
        for (Transfer ts : transfers) {
            somme_mont += ts.getMontant();
        }
        if (somme_mont > 20000)
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("plafond maximale est atteint");
        transfer.setRef(Generator.getRef());
        transfer.setCodePin(Generator.getPin());
        transfer.setStatus(Status.Aservir);
        transfer.setTransferDate(LocalDateTime.now());
        transfer.setExprDate(LocalDateTime.now().plusDays(15));
        if (transfer.getModeCost().equals(ModeCost.Destination))
            transfer.setMontant(transfer.getMontant() - (transfer.getMontant() / 10));
        if (transfer.getModeCost().equals(ModeCost.Partagé))
            transfer.setMontant(transfer.getMontant() - (transfer.getMontant() / 20));
        if (transfer.getMode().equals(EmissionType.ByGab)) {
            if (transfer.getMontant() > soldAgent || transfer.getMontant() > 2000)
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Votre solde est insuffisant");
            else {
                JSONObject personJsonObject = new JSONObject();
                Client client_destination = restTemplate.getForObject("http://CLIENT-SERVICE/client/"+transfer.getClientDst(),Client.class);
                personJsonObject.put("phone",client_destination.getPhone());
                personJsonObject.put("message", transfer.getCodePin());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> request = new HttpEntity<String>(personJsonObject.toString(), headers);
                ResponseEntity<String> code = restTemplate.postForEntity("http://NOTIFICATION-SERVICE/notification/send", request, String.class);
                if (code.getStatusCode().value() == 200)
                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(transferRepository.save(transfer));
                else
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body("Notification not send");
            }
        } else {
            if (transfer.getMontant() > soldAgent)
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Votre solde est insuffisant");
            else
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(transferRepository.save(transfer));
        }
    }

    public ResponseEntity<?> transferEspeceAgentMult(List<Transfer> transfers, double soldAgent) {
        double old_transfer = 0;
        List<Transfer> tss = transferRepository.findByClientSrc(transfers.get(0).getClientSrc());
        for (Transfer transfer : tss) old_transfer += transfer.getMontant();
        double new_transfer = 0;
        for (Transfer ts : transfers) {
            new_transfer += ts.getMontant();
            if (ts.getMontant() > 2000 && ts.getMode().equals(EmissionType.ByGab))
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Erreur s'est produite");
        }
        if ((new_transfer + old_transfer) > 20000)
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Vous avez depasse le plafond annuel");
        if (new_transfer > soldAgent)
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Votre solde est insuffisant");
        List<Transfer> succes_transfers = new ArrayList<Transfer>();
        for (Transfer transfer : transfers) {
            Transfer ts = (Transfer) transferEspeceAgent(transfer, soldAgent).getBody();
            succes_transfers.add(ts);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(succes_transfers);
    }

    public ResponseEntity<?> myTransfers(Long clientSrc) {
        Client client_source = restTemplate.getForObject("http://CLIENT-SERVICE/client/"+clientSrc,Client.class);
        List<Transfer> transfers = transferRepository.findTransferByClientSrcOrClientDst(clientSrc,clientSrc);
        List<TransferWithClients> transfersWithClients = new ArrayList<>();
        if(transfers.size() == 0)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Le client n'a effectué aucun transfert");
        else{
            for(Transfer ts:transfers){
                Client client_dst = restTemplate.getForObject("http://CLIENT-SERVICE/client/"+ts.getClientDst(),Client.class);
                Client client_source1 = restTemplate.getForObject("http://CLIENT-SERVICE/client/"+ts.getClientSrc(),Client.class);
                TransferWithClients transferWithClients = new TransferWithClients();
                transferWithClients.setTransfer(ts);
                transferWithClients.setClientDst(client_dst);
                transferWithClients.setClientSrc(client_source1);
                transfersWithClients.add(transferWithClients);
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(transfersWithClients);
        }
    }

    public ResponseEntity<?> transferDebAgent(Transfer transfer,double soldCompte) {
        List<Transfer> transfers = transferRepository.findByClientSrc(transfer.getClientSrc());
        double somme_mont = 0;
        for (Transfer ts : transfers) {
            somme_mont += ts.getMontant();
        }
        if (somme_mont > 20000)
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("plafond maximale est atteint");
        transfer.setRef(Generator.getRef());
        transfer.setCodePin(Generator.getPin());
        transfer.setStatus(Status.Aservir);
        transfer.setTransferDate(LocalDateTime.now());
        transfer.setExprDate(LocalDateTime.now().plusDays(15));
        double frais = 0;
        double montantTransfer = transfer.getMontant();
        if (transfer.getModeCost().equals(ModeCost.Destination))
            transfer.setMontant(transfer.getMontant() - (transfer.getMontant() / 10));
        if (transfer.getModeCost().equals(ModeCost.Partagé)){
            frais = transfer.getMontant() / 20;
            transfer.setMontant(transfer.getMontant() - (transfer.getMontant() / 20));
        }else if(transfer.getModeCost().equals(ModeCost.Source)){
            frais = transfer.getMontant() / 10;
        }

        if (transfer.getMode().equals(EmissionType.ByGab)) {
            if (transfer.getMontant() > 2000 || transfer.getMontant() > soldCompte)
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Erreur s'est produite");
            else {
                JSONObject personJsonObject = new JSONObject();
                Client client_destination = restTemplate.getForObject("http://CLIENT-SERVICE/client/"+transfer.getClientDst(),Client.class);
                personJsonObject.put("phone",client_destination.getPhone());
                personJsonObject.put("message", transfer.getCodePin());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> request = new HttpEntity<String>(personJsonObject.toString(), headers);
                ResponseEntity<String> code = restTemplate.postForEntity("http://NOTIFICATION-SERVICE/notification/send", request, String.class);
                if (code.getStatusCode().value() == 200){
                    removeMoney(montantTransfer+frais,transfer.getClientSrc());
                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(transferRepository.save(transfer));
                }
                else
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body("Notification not send");
            }
        } else {
            if (transfer.getMontant() > soldCompte)
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Votre solde est insuffisant");
            else {
                removeMoney(montantTransfer+frais,transfer.getClientSrc());
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(transferRepository.save(transfer));
            }
        }
    }

    public ResponseEntity<?> transferDebitAgentMult(List<Transfer> transfers, double soldCompte) {
        double old_transfer = 0;
        List<Transfer> tss = transferRepository.findByClientSrc(transfers.get(0).getClientSrc());
        for (Transfer transfer : tss) old_transfer += transfer.getMontant();
        double new_transfer = 0;
        for (Transfer ts : transfers) {
            new_transfer += ts.getMontant();
            if (ts.getMontant() > 2000 && ts.getMode().equals(EmissionType.ByGab))
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Erreur s'est produite");
        }
        if ((new_transfer + old_transfer) > 20000)
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Vous avez depasse le plafond annuel");
        if (new_transfer > soldCompte)
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Votre solde est insuffisant");
        List<Transfer> succes_transfers = new ArrayList<Transfer>();
        for (Transfer transfer : transfers) {
            Transfer ts = (Transfer) transferEspeceAgent(transfer, soldCompte).getBody();
            succes_transfers.add(ts);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(succes_transfers);
    }

    public Transfer blockTransfer(Long id) {
        Transfer transfer = transferRepository.findById(id).get();
        if (!transfer.getStatus().equals(Status.Bloqué)) {
            transfer.setStatus(Status.Bloqué);
            return transferRepository.save(transfer);
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Le transfert est deja bloqué !");
    }

    public Transfer deblockTransfer(Long id) {
        Transfer transfer = transferRepository.findById(id).get();
        if (transfer.getStatus().equals(Status.Bloqué)) {
            transfer.setStatus(Status.Debloqué);
            return transferRepository.save(transfer);
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Le transfert n'est pas bloqué !");
    }

    public Transfer extournTransfer(Long id) {
        Transfer transfer = transferRepository.findById(id).get();
        if (transfer.getStatus().equals(Status.Aservir)) {
            addMoney(transfer.getMontant(),transfer.getClientSrc());
            transfer.setStatus(Status.Extourné);
            return transferRepository.save(transfer);
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Le transfert est bloqué !");
    }

    public Transfer restoreTransfer(Long id) {
        Transfer transfer = transferRepository.findById(id).get();
        if (transfer.getStatus().equals(Status.Aservir)) {
            addMoney(transfer.getMontant(),transfer.getClientSrc());
            transfer.setStatus(Status.Restitué);
            return transferRepository.save(transfer);
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Le transfert est bloqué !");
    }

    public List<Transfer> transfersByStatus(Status status) {
        return transferRepository.findTransferByStatus(status);
    }

    public List<Transfer> getExpiredTransfers() {
        List<Transfer> transfers = transferRepository.findByExprDateBefore(LocalDateTime.now());
        for (Transfer transfer : transfers) {
            if (transfer.getStatus().equals(Status.Aservir)) {
                transfer.setStatus(Status.Endeshérence);
                transferRepository.save(transfer);
            }
        }
        return transfers;
    }
    public ResponseEntity<?> getTransferById(Long id){
        Transfer transfer = transferRepository.findById(id).get();
        //HttpHeaders responseHeaders = new HttpHeaders();
        //responseHeaders.set("access-control-allow-origin","*");
        if(transfer!=null){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    //.headers(responseHeaders)
                    .body(transfer);
        }
        else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Pas de Transfer avec cet id");
        }
    }
    public ResponseEntity<?> transferByWallet(Transfer transfer) {
        List<Transfer> transfers = transferRepository.findByClientSrc(transfer.getClientSrc());
        double somme_mont = 0;
        for (Transfer ts : transfers) {
            somme_mont += ts.getMontant();
        }
        if (somme_mont > 20000)
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("plafond maximale est atteint");
        Compte compte_src = restTemplate.getForObject("http://AGENT-SERVICE/agent/compte/client/"+transfer.getClientSrc(),Compte.class);
        double montantTransfer  = transfer.getMontant();
        double frais = 0;
        if (transfer.getModeCost().equals(ModeCost.Destination))
            transfer.setMontant(transfer.getMontant() - (transfer.getMontant() / 10));
        if (transfer.getModeCost().equals(ModeCost.Partagé)){
            transfer.setMontant(transfer.getMontant() - (transfer.getMontant() / 20));
            frais = transfer.getMontant()/20;
        }
        if(transfer.getModeCost().equals(ModeCost.Source))
            frais = transfer.getMontant()/10;
        if(compte_src.getSolde()<(montantTransfer+frais))
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Solde insuffisant !");
        if(transfer.getMode().equals(EmissionType.ToAccount)){
            Compte compte_dst = restTemplate.getForObject("http://AGENT-SERVICE/agent/compte/client/"+transfer.getClientDst(),Compte.class);
            if(compte_dst == null)
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Le client n'a pas un compte !!");
            else{
                addMoney(transfer.getMontant(),transfer.getClientDst());
                removeMoney(montantTransfer+frais,transfer.getClientSrc());
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(transferRepository.save(transfer));
            }
        }else{
            transfer.setRef(Generator.getRef());
            transfer.setCodePin(Generator.getPin());
            transfer.setStatus(Status.Aservir);
            transfer.setTransferDate(LocalDateTime.now());
            transfer.setExprDate(LocalDateTime.now().plusDays(15));
            if (transfer.getMode().equals(EmissionType.ByGab)) {
                JSONObject personJsonObject = new JSONObject();
                Client client_destination = restTemplate.getForObject("http://CLIENT-SERVICE/client/"+transfer.getClientDst(),Client.class);
                personJsonObject.put("phone",client_destination.getPhone());
                personJsonObject.put("message", transfer.getCodePin());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> request = new HttpEntity<String>(personJsonObject.toString(), headers);
                ResponseEntity<String> code = restTemplate.postForEntity("http://NOTIFICATION-SERVICE/notification/send", request, String.class);
                if (code.getStatusCode().value() == 200){
                    removeMoney(montantTransfer+frais,transfer.getClientSrc());
                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(transferRepository.save(transfer));
                }
                else
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body("Notification not send");
            } else {
                removeMoney(montantTransfer+frais,transfer.getClientSrc());
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(transferRepository.save(transfer));
            }
        }
    }
    public ResponseEntity<?> transferByWalletMult(List<Transfer> transfers){
        List<Transfer> successTransfers = new ArrayList<>();
        for(Transfer ts:transfers){
           Transfer st = (Transfer) transferByWallet(ts).getBody();
           if(st != null) successTransfers.add(st);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(successTransfers);
    }
    public void removeMoney(double moneyValue,Long idClient){
        JSONObject payload = new JSONObject();
        payload.put("moneyValue",moneyValue);
        payload.put("idClient",idClient);
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request1 = new HttpEntity<String>(payload.toString(), headers1);
        restTemplate.put("http://AGENT-SERVICE/agent/compte/removeMoney/",request1,Compte.class);
    }
    public void addMoney(double moneyValue,Long idClient){
        JSONObject payload = new JSONObject();
        payload.put("moneyValue",moneyValue);
        payload.put("idClient",idClient);
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request1 = new HttpEntity<String>(payload.toString(), headers1);
        restTemplate.put("http://AGENT-SERVICE/agent/compte/addMoney/",request1,Compte.class);
    }
}

