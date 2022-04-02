package com.practica.ibm.auth.payloads;

// { "email": "exemplu", "password": "abc" }

import lombok.Getter;
import lombok.Setter;

public class SignupRequestBody {

    private @Getter @Setter String email;

    private @Getter @Setter String password;

}
