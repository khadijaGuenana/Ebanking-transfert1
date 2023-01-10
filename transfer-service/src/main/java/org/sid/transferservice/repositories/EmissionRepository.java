package org.sid.transferservice.repositories;

import org.sid.transferservice.entities.Emission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmissionRepository extends JpaRepository<Emission,Long> {

}
