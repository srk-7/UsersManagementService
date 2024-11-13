package com.achievers.UserAuthentication.service;

import com.achievers.UserAuthentication.dto.RegisterRequestDto;
import com.achievers.UserAuthentication.model.Users;
import com.achievers.UserAuthentication.model.Admins;
import com.achievers.UserAuthentication.model.Customers;
import com.achievers.UserAuthentication.repository.UserRepository;
import com.achievers.UserAuthentication.repository.AdminRepository;
import com.achievers.UserAuthentication.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getHashedPassword(),
                user.getRole().equals("ADMIN") ? List.of(new SimpleGrantedAuthority("ROLE_ADMIN")) : List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
    }

    public ResponseEntity<String> registerAdmin(RegisterRequestDto registerRequestDTO) {
        String uid = UUID.randomUUID().toString();

        Users user = new Users(uid, registerRequestDTO.getEmailId(), passwordEncoder.encode(registerRequestDTO.getPassword()), "ADMIN");
        usersRepository.save(user);
        Admins admin = new Admins();
        admin.setUid(uid);
        admin.setFullName(registerRequestDTO.getFullName());
        admin.setAddress(registerRequestDTO.getAddress());
        admin.setEmailId(registerRequestDTO.getEmailId());
        admin.setMobileNumber(registerRequestDTO.getMobileNumber());
        admin.setAge(registerRequestDTO.getAge());
        admin.setGender(registerRequestDTO.getGender());

        adminRepository.save(admin);

        return ResponseEntity.status(HttpStatus.CREATED).body("Admin registered successfully");
    }

    public ResponseEntity<String> registerCustomer(RegisterRequestDto registerRequestDTO) {
        String uid = UUID.randomUUID().toString();

        Users user = new Users(uid, registerRequestDTO.getEmailId(), passwordEncoder.encode(registerRequestDTO.getPassword()), "CUSTOMER");
        usersRepository.save(user);

        Customers customer = new Customers();
        customer.setUid(uid);
        customer.setFullName(registerRequestDTO.getFullName());
        customer.setAddress(registerRequestDTO.getAddress());
        customer.setEmailId(registerRequestDTO.getEmailId());
        customer.setMobileNumber(registerRequestDTO.getMobileNumber());
        customer.setAge(registerRequestDTO.getAge());
        customer.setGender(registerRequestDTO.getGender());

        customerRepository.save(customer);

        return ResponseEntity.status(HttpStatus.CREATED).body("Customer registered successfully");
    }
}
