package org.sid.transferservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmissionData {
    private Long transfer;
    private Long agent;
    private Long agence;
}
