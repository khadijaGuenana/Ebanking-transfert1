package com.ensa.backofficeservice.repository;

import com.ensa.backofficeservice.domains.BackOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackofficeRepository extends JpaRepository<BackOffice,Long> {
    public BackOffice findBackOfficeByUsernameAndPassword(String username,String password);
}
