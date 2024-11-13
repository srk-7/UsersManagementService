package com.achievers.UserAuthentication.repository;

import com.achievers.UserAuthentication.model.Customers;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customers,String> {
}
