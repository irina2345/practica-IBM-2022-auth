package com.practica.ibm.auth.controllers;

import com.practica.ibm.auth.payloads.SignupRequestBody;
import com.practica.ibm.auth.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity signup(@RequestBody SignupRequestBody signupRequestBody) {
        System.out.println("Signup request received");

        List<String> errors = authService.signup(signupRequestBody);

        if (errors.isEmpty()) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
