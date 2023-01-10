package org.sid.transferservice.service;

import org.json.JSONObject;
import org.sid.transferservice.entities.Client;
import org.sid.transferservice.entities.Emission;
import org.sid.transferservice.entities.EmissionData;
import org.sid.transferservice.entities.Transfer;
import org.sid.transferservice.enums.EmissionType;
import org.sid.transferservice.enums.Status;
import org.sid.transferservice.repositories.EmissionRepository;
import org.sid.transferservice.repositories.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class EmissionService {

    private EmissionRepository emissionRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TransferRepository transferRepository;

    public EmissionService(EmissionRepository emissionRepository) {
        this.emissionRepository = emissionRepository;
    }

    public ResponseEntity<?> emissionByAgent(EmissionData data) {
        Transfer transfer = transferRepository.findById(data.getTransfer()).get();
        if (transfer.getStatus().equals(Status.Aservir) || transfer.getStatus().equals(Status.Debloqué)) {
            Emission emission = new Emission();
            emission.setTransfer(transfer);
            emission.setAgent(data.getAgent());
            emission.setAgence(data.getAgence());
            transfer.setStatus(Status.Servie);
            transferRepository.save(transfer);
            emission.setEmissionType(EmissionType.ByAgent);
            emission.setEmissionDate(LocalDateTime.now());
            JSONObject personJsonObject = new JSONObject();
            Client client_source = restTemplate.getForObject("http://CLIENT-SERVICE/client/"+transfer.getClientSrc(),Client.class);
            personJsonObject.put("phone", client_source.getPhone());
            personJsonObject.put("message", "Le transfert  " + emission.getTransfer().getRef() + " a été sérvi");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<String>(personJsonObject.toString(), headers);
            ResponseEntity<String> code = restTemplate.postForEntity("http://NOTIFICATION-SERVICE/notification/send", request, String.class);
            if (code.getStatusCode().value() == 200)
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(emissionRepository.save(emission));
            else
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Notification not send");
        }else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Le transfert n'est a servir");
        }
    }
    public ResponseEntity<?> emissionByGab(EmissionData data){
        Transfer transfer = transferRepository.findById(data.getTransfer()).get();
        if(transfer.getStatus().equals(Status.Aservir) || transfer.getStatus().equals(Status.Debloqué)){
            Emission emission = new Emission();
            emission.setTransfer(transfer);
            emission.setAgent(data.getAgent());
            emission.setAgence(data.getAgence());
            transfer.setStatus(Status.Servie);
            transferRepository.save(transfer);
            emission.setEmissionType(EmissionType.ByGab);
            emission.setEmissionDate(LocalDateTime.now());
            JSONObject personJsonObject = new JSONObject();
            Client client_source = restTemplate.getForObject("http://CLIENT-SERVICE/client/"+transfer.getClientSrc(),Client.class);
            personJsonObject.put("phone",client_source.getPhone());
            personJsonObject.put("message","Le transfert  "+emission.getTransfer().getRef()+" est sérvi");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<String>(personJsonObject.toString(), headers);
            ResponseEntity<String> code = restTemplate.postForEntity("http://NOTIFICATION-SERVICE/notification/send", request,String.class);
            if(code.getStatusCode().value()==200)
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(emissionRepository.save(emission));
            else
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Notification not send");
        }else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Le transfert n'est pas a servir");
        }
    }

    public ResponseEntity<?> getTransferByPinAndRef(Transfer transfer){
        Transfer ts = transferRepository.findTransferByCodePinAndRef(transfer.getCodePin(),transfer.getRef());
        if(ts!=null)
            return ResponseEntity

                    .status(HttpStatus.OK)
                    .body(ts);
        else
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Aucun transfert existe avec ces cordonnées");
    }

}
