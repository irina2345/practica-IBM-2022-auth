package com.practica.ibm.auth.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("accounts")
public class Account {

    private @Getter @Setter String email;

    private @Getter @Setter byte[] passwordHash;

    private @Getter @Setter byte[] passwordSalt;

}
