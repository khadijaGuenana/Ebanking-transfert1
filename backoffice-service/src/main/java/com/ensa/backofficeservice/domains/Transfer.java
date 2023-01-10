package com.ensa.backofficeservice.domains;

import com.ensa.backofficeservice.enums.EmissionType;
import com.ensa.backofficeservice.enums.ModeCost;
import com.ensa.backofficeservice.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor @NoArgsConstructor @ToString
public class Transfer{

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

}
