package com.practica.ibm.auth.controllers;

import com.practica.ibm.auth.payloads.SigninSignupRequestBody;
import com.practica.ibm.auth.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("signup")
    public ResponseEntity signup(@RequestBody SigninSignupRequestBody signupRequestBody) {
        System.out.println("Signup request received");

        List<String> errors = authService.signup(signupRequestBody);

        if (errors.isEmpty()) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("signin")
    public ResponseEntity signin(@RequestBody SigninSignupRequestBody signinRequestBody) {
        System.out.println("Signin request received");

        return  authService.signin(signinRequestBody);
    }

}
