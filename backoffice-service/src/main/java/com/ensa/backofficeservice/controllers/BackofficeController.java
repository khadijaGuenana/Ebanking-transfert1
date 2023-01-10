package com.ensa.backofficeservice.controllers;

import com.ensa.backofficeservice.domains.BackOffice;
import com.ensa.backofficeservice.domains.Transfer;
import com.ensa.backofficeservice.service.BackofficeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backoffice")
@AllArgsConstructor
public class BackofficeController {
    private BackofficeService backofficeService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> loginBackoffice(@RequestBody BackOffice backOffice){
        return backofficeService.login(backOffice);
    }

    @PostMapping("/addTransfer")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addTransfer(@RequestBody Transfer transfer){
        return backofficeService.addTransfertEspece(transfer);
    }
    @PostMapping("/addTransferMult")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addTransferMult(@RequestBody List<Transfer> transfers){
        return backofficeService.addTransfertEspeceMult(transfers);
    }



}
