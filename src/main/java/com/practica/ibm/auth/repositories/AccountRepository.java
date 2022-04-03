package com.practica.ibm.auth.repositories;

import com.practica.ibm.auth.models.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    @Query("{ email: ?0 }")
    Optional<Account> getAccountByEmail();


}
