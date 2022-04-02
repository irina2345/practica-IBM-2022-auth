package com.practica.ibm.auth.services;

import com.practica.ibm.auth.models.Account;
import com.practica.ibm.auth.payloads.SignupRequestBody;
import com.practica.ibm.auth.repositories.AccountRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    AccountRepository accountRepository;

    public List<String> signup(SignupRequestBody signupRequestBody) {
        List<String> errors = new ArrayList<>();

        // TODO
        // exista deja un cont cu acest mail? daca da adauga in lista de erori
        // Optional<Account> optionalAccount = accountRepository.getAccountByEmail();
        // optionalAccount.isPresent();

        Account account = new Account();
        account.setEmail(signupRequestBody.getEmail());
        byte[] salt = generateSalt();
        account.setPasswordSalt(salt);
        byte[] hash = generateHash(signupRequestBody.getPassword(), salt);
        account.setPasswordHash(hash);
        accountRepository.save(account);

        return errors;
    }

    private byte[] generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

    @SneakyThrows
    private byte[] generateHash(String password, byte[] salt) {
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 2048, 128);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWithHmacSHA512AndaES_256");
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        return secretKey.getEncoded();
    }

}
