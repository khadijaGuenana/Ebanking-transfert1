package org.sid.transferservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.transferservice.enums.EmissionType;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Emission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JoinColumn(name="TRANSFER")
    @OneToOne
    private Transfer transfer;
    private Long agent;
    private  Long agence;
    @Enumerated(EnumType.STRING)
    private EmissionType emissionType; //By Agent - By Gab
    private LocalDateTime emissionDate;
}
