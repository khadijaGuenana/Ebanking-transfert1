package com.ensa.serviceclient.repositories;

import com.ensa.serviceclient.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepo extends JpaRepository<Client,Long> {
    public Client findByPhone(String phone);
}
