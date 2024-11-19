package com.achievers.UserAuthentication.repository;

import com.achievers.UserAuthentication.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
// shivaramakrishna
public interface UserRepository extends MongoRepository<Users, String> {
    Optional<Users> findByUsername(String username);
    void deleteByUid(String uid);

}