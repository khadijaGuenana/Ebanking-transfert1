package com.ensa.backofficeservice.service;

import com.ensa.backofficeservice.domains.BackOffice;
import com.ensa.backofficeservice.domains.Transfer;
import com.ensa.backofficeservice.repository.BackofficeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackofficeService {
    private BackofficeRepository backofficeRepository;
    private RestTemplate restTemplate;
    /*
        public void batchBlocage() ;
        public void batchDesherence();
     */
    public ResponseEntity<?> login(BackOffice backOffice){
        if(backOffice.getUsername()!=null && backOffice.getPassword()!=null){
            BackOffice foundAccount = backofficeRepository.findBackOfficeByUsernameAndPassword(backOffice.getUsername(),backOffice.getPassword());
            if(foundAccount != null)
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(foundAccount);
            else
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Username ou mot de passe invalides");
        }
        else{
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Username ou mot de passe vide");
        }
    }
    public ResponseEntity<?> addTransfertEspece(Transfer transfert) {
        Transfer tr = restTemplate.postForObject("http://TRANSFER-SERVICE/transferservice/transfer/cash/200000",transfert,Transfer.class);
        if(tr != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(tr);
        }else{
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erreur s'est produite !");
        }
    }
    public ResponseEntity<?> addTransfertEspeceMult(List<Transfer> listetransferts) {
        List<Transfer> listetr= Arrays.asList(restTemplate.postForObject("http://TRANSFER-SERVICE/transferservice/transfer/cash/multiple/2000000",listetransferts,Transfer[].class));
        if(listetr != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(listetr);
        }else{
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erreur s'est produite !");
        }
    }

}