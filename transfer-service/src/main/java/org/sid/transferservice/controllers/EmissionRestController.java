package org.sid.transferservice.controllers;


import org.sid.transferservice.entities.EmissionData;
import org.sid.transferservice.entities.Transfer;
import org.sid.transferservice.service.EmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/transferservice/emission")
public class EmissionRestController {
    EmissionService emissionService;
    public EmissionRestController(EmissionService emissionService) {
        this.emissionService=emissionService;
    }
    @PostMapping(path = "/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> findTransferByPinAndRef(@RequestBody Transfer transfer){
        return emissionService.getTransferByPinAndRef(transfer);
    }
    @PostMapping(path = "/emissionByAgent")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> emissionByAgent(@RequestBody EmissionData data){
        return  emissionService.emissionByAgent(data);
    }
    @PostMapping(path = "/emissionByGab")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> emissionByGab(@RequestBody EmissionData data){
        return  emissionService.emissionByGab(data);
    }
}
