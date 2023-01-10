package com.ensa.serviceclient.entities;

import com.ensa.serviceclient.enums.transfertService.EmissionType;
import com.ensa.serviceclient.enums.transfertService.ModeCost;
import com.ensa.serviceclient.enums.transfertService.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {

    private Long id;
    private String ref;
    private String codePin;
    private double montant;
    private Long clientSrc;
    private Long clientDst;
    private Status status;
    private ModeCost modeCost;//Source - Destination - Partagé
    private EmissionType mode; //ByGab-ByAgent
    private LocalDateTime transferDate;
    private LocalDateTime exprDate;
    //add new attribute motif of blocage
    private String motifBlocage;
}
