package com.achievers.UserAuthentication.repository;

import com.achievers.UserAuthentication.model.Admins;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminRepository extends MongoRepository<Admins,String> {
    Optional<Object> findByUid(String uid);
    boolean existsByUid(String uid);

}