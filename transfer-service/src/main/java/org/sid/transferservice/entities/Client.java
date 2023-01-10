package org.sid.transferservice.entities;


import lombok.Data;


@Data
public class Client {

    private Long id;
    private String firstName;
    private String secondName;
    private String cne;
    private String phone;
}
