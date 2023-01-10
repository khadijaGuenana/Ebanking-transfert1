package org.sid.transferservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferWithClients {

    private Transfer transfer;
    private Client clientSrc;
    private Client clientDst;
}
