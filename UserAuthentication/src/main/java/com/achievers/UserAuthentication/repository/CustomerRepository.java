package com.achievers.UserAuthentication.repository;

import com.achievers.UserAuthentication.model.Customers;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customers,String> {
    Optional<Object> findByUid(String uid);
    boolean existsByUid(String uid);

}