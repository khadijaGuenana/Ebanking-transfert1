package org.sid.transferservice.repositories;

import org.sid.transferservice.entities.Transfer;
import org.sid.transferservice.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TransferRepository extends JpaRepository<Transfer,Long> {
    public List<Transfer> findByClientSrc(Long clientSrc);
    public Transfer findTransferByCodePinAndRef(String codePing,String ref);
    public List<Transfer> findTransferByStatus(Status status);
    public List<Transfer> findByExprDateBefore(LocalDateTime date);
    public List<Transfer> findTransferByClientSrcOrClientDst(Long clientSrc,Long clientDst);
}
