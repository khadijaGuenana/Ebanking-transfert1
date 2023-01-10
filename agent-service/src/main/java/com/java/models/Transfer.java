package com.java.models;

import java.time.LocalDateTime;

import com.java.enums.EmissionType;
import com.java.enums.ModeCost;
import com.java.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor @NoArgsConstructor @ToString

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
