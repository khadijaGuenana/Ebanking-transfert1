package org.sid.transferservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.transferservice.enums.EmissionType;
import org.sid.transferservice.enums.ModeCost;
import org.sid.transferservice.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String ref;
    private String codePin;
    private double montant;
    private Long clientSrc;
    private Long clientDst;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private ModeCost modeCost;//Source - Destination - Partagé
    @Enumerated(EnumType.STRING)
    private EmissionType mode; //ByGab-ByAgent-ToAccount
    private LocalDateTime transferDate;
    private LocalDateTime exprDate;
}
