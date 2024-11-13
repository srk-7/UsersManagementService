package com.achievers.UserAuthentication.repository;

import com.achievers.UserAuthentication.model.Admins;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<Admins,String> {
}