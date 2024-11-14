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

    public ResponseEntity<String> updateAdmin(String uid, RegisterRequestDto registerRequestDto) {
        Admins admin = (Admins) adminRepository.findByUid(uid).orElse(null);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
        }

        admin.setFullName(registerRequestDto.getFullName());
        admin.setAddress(registerRequestDto.getAddress());
        admin.setEmailId(registerRequestDto.getEmailId());
        admin.setMobileNumber(registerRequestDto.getMobileNumber());
        admin.setAge(registerRequestDto.getAge());
        admin.setGender(registerRequestDto.getGender());
        adminRepository.save(admin);

        Users user = usersRepository.findById(uid).orElse(null);
        if (user != null) {
            user.setUsername(registerRequestDto.getEmailId());
            user.setHashedPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
            usersRepository.save(user);
        }

        return ResponseEntity.ok("Admin updated successfully");
    }

    public ResponseEntity<String> updateCustomer(String uid, RegisterRequestDto registerRequestDto) {
        Customers customer = (Customers) customerRepository.findByUid(uid).orElse(null);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }

        customer.setFullName(registerRequestDto.getFullName());
        customer.setAddress(registerRequestDto.getAddress());
        customer.setEmailId(registerRequestDto.getEmailId());
        customer.setMobileNumber(registerRequestDto.getMobileNumber());
        customer.setAge(registerRequestDto.getAge());
        customer.setGender(registerRequestDto.getGender());
        customerRepository.save(customer);

        Users user = usersRepository.findById(uid).orElse(null);
        if (user != null) {
            user.setUsername(registerRequestDto.getEmailId());
            user.setHashedPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
            usersRepository.save(user);
        }

        return ResponseEntity.ok("Customer updated successfully");
    }

    public ResponseEntity<String> deleteAdmin(String uid) {
        if (!adminRepository.existsByUid(uid)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
        }

        adminRepository.deleteById(uid);
        usersRepository.deleteById(uid);
        return ResponseEntity.ok("Admin deleted successfully");
    }

    public ResponseEntity<String> deleteCustomer(String uid) {
        if (!customerRepository.existsByUid(uid)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }

        customerRepository.deleteById(uid);
        usersRepository.deleteById(uid);
        return ResponseEntity.ok("Customer deleted successfully");
    }

    public ResponseEntity<Admins> getAdminById(String uid) {
        Admins admin = (Admins) adminRepository.findByUid(uid).orElse(null);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(admin);
    }

    public ResponseEntity<Customers> getCustomerById(String uid) {
        Customers customer = (Customers) customerRepository.findByUid(uid).orElse(null);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(customer);
    }


}
