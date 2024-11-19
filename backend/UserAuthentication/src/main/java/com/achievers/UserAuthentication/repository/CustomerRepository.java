package com.achievers.UserAuthentication.repository;

import com.achievers.UserAuthentication.model.Customers;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
// shivaramakrishna
public interface CustomerRepository extends MongoRepository<Customers,String> {
    Optional<Object> findByUid(String uid);
    boolean existsByUid(String uid);
    void deleteByUid(String uid);
    Optional<Object> findByEmailId(String email);
    boolean existsByEmailId(String emailId);

}