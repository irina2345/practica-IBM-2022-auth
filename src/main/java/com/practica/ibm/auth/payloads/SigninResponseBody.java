package com.practica.ibm.auth.payloads;

import lombok.Getter;
import lombok.Setter;

public class SigninResponseBody {

    private @Getter @Setter String token;

    private @Getter @Setter String email;

}
