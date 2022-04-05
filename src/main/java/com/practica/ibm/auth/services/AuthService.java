package com.practica.ibm.auth.services;

import com.practica.ibm.auth.models.Account;
import com.practica.ibm.auth.payloads.SigninResponseBody;
import com.practica.ibm.auth.payloads.SigninSignupRequestBody;
import com.practica.ibm.auth.repositories.AccountRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    AccountRepository accountRepository;

    public static final String EMAIL = "email";

    private String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDyN5fgFl6VYJZw" +
            "5EJ7rRmXJ83/IIRJfVCmK5p2h+UwxR9DDvcLNlqWQ4sWAgbDI5X4l6xsV+WZQNAr" +
            "0EJvAurevUrAyZ+LWBhLOg1sWylPtgONOLRGUF3ZlbBuh60YfBBC1jZlSKyV1rHP" +
            "D7Kiinzj002SwGpkpuzcY1siDknn4cmSrFvhzhO5Mc/SVe5FQ5J1JQgcKRHYaOI9" +
            "rFpuAevz232f9vncbloK0ruQBmQfsxS7e5RUjuRdw9NW+FfzUTE46LJNJMu+WC7O" +
            "lUjD3fEV7OfoueN1YN20gsGqRRDcHu8ZqaHnt5BwoG9IfLlNRtZ6yrt9nFME3Xhf" +
            "UNSbix4RAgMBAAECggEAL2zzL80xd84RpskKu2yJNi91Bh83x6vhvqeK9upk1cfC" +
            "9z5W4LsjwYg4E3cSG+M72hDYkd4j9n3aRLniSBoqIrKtkFHqCz6UgyY6upye2hyp" +
            "VV1k7t+NgKzufrXSDpk8FdobjHlOUYhYCFLqhyjWi0U0SyehrVOBzSUnEuP5VI1f" +
            "f8dsdWND19bR0iHrnFbqZ5FiCRccmU7EUUzGN7dGAYuBf928IF2AhJmHElOLYLWb" +
            "8Yj9y00RKK4vd5nYzZdw88sZDOmzos6xKoVQcYiWHiNCWiyicDYuGfCi3a5YzA2m" +
            "xi1v6EDkXG0t1cuP7qHRRt19421paZe5EMUw9VluAQKBgQD/B5kdxXfx0UNFaa4d" +
            "bX/vgu2185M2hvRWZaInT1gsDp5/DXZNUzXI4ne9Vr0K5aKL2JKj4ZTOjnTnVY6B" +
            "u68J5WfxiR2lAN+GonF2dCuJh++OiGJU9+7MTrhlUPanknILicUsYC/U6i7HyoM6" +
            "gHFEhNt4VUWtp5eJICI6+wOyXQKBgQDzI4P++OkZtFPaq0tK0A2OjCfOwfBlhjdO" +
            "jkhLSh/mV802hmNjKE+ILZTa4ARyW+FcDjkShKKxyO/00+ai/yE5naL4d11TLciy" +
            "hbTW9nWafiljpFotg/oU6AzRh5D+yTo9LnOozrJBAU5WWv59ADDdHn9yYZHC6Q+e" +
            "AAPK5KGHRQKBgQCWqKpvyAQ/utv+2Zuejt2RZMuBPc+t2rPlhkzw5BK2lDxV2mIU" +
            "9B9F+WO7Ws567YXFD8AxSEZccBPnxcdWJMYXFor9SX9VxtpF8xkMXTyLFt8SWMee" +
            "YomT8L1GPTo5bG4rGsx8lqYrJd1U8fHsUYyYadQtllJ/cA45V95g8YERkQKBgQCv" +
            "g4rMa0hx2r/RkWe3qLqwlTJDSbLMxvdzyLgdSXP/ppAn9RkXwkivSCA1ZBMxrmvf" +
            "NREz8fU0G5ZEiRbrwPpuCgJUFG6VFe7UGDr0FlPM45fjKwhB5QyJPP3L323mzsIw" +
            "2jB5wkPhMx0mr2cYOyQK6vIbUp2TMwSoDOCv/R0xdQKBgD6QRy+Y0duh4EOfKJfB" +
            "LE9BVHsfuAQDS1hlHGBcBfBYR6W2ynsbWF19rPznTxuX1B1K7DTzgV3fuaLD0pdG" +
            "o01EocgPFDvm8d7HiRky1LaqBHc2cann29hjCcWLV4QeWSV+xql0lFz0JSdb5ouW" +
            "rjJMU5eaUXy/y8wrqI0RufcF";

    public List<String> signup(SigninSignupRequestBody signupRequestBody) {
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

    public ResponseEntity signin(SigninSignupRequestBody signinRequestBody) {
        List<String> errors = new ArrayList<>();
        Optional<Account> accountOptional = accountRepository.getAccountByEmail(signinRequestBody.getEmail());

        if (!accountOptional.isPresent()) {
            errors.add("Nu exista nici un cont cu mailul: " + signinRequestBody.getEmail());
        } else {
            Account account = accountOptional.get();

            byte[] generatedHash = generateHash(signinRequestBody.getPassword(), account.getPasswordSalt());
            if (!Arrays.equals(generatedHash, account.getPasswordHash())) {
                errors.add("Parola introdusa e gresita");
            } else {
                SigninResponseBody signinResponseBody = new SigninResponseBody();
                signinResponseBody.setToken(generateToken(signinRequestBody.getEmail()));
                signinResponseBody.setEmail(signinRequestBody.getEmail());
                return ResponseEntity.ok().body(signinResponseBody);
            }
        }

        return ResponseEntity.badRequest().body(errors);
    }

    private String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(EMAIL, email);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 4320 * 1000))
                .signWith(SignatureAlgorithm.RS256, getPrivateSigningKey())
                .compact();

    }

    @SneakyThrows
    private PrivateKey getPrivateSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(keySpec);
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
