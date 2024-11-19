package com.achievers.UserAuthentication.controller;

import com.achievers.UserAuthentication.dto.LoginRequestDto;
import com.achievers.UserAuthentication.dto.RegisterRequestDto;
import com.achievers.UserAuthentication.model.Admins;
import com.achievers.UserAuthentication.model.Customers;
import com.achievers.UserAuthentication.repository.AdminRepository;
import com.achievers.UserAuthentication.repository.CustomerRepository;
import com.achievers.UserAuthentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/register/admin") // shivaramakrishna
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterRequestDto registerRequestDto) {

        if (adminRepository.existsByEmailId(registerRequestDto.getEmailId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email ID is already registered as an Admin.");
        }


        if (customerRepository.existsByEmailId(registerRequestDto.getEmailId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email ID is already registered as a Customer.");
        }

        return userService.registerAdmin(registerRequestDto);
    }

    @PostMapping("/register/customer") // shivaramakrishna
    public ResponseEntity<String> registerCustomer(@RequestBody RegisterRequestDto registerRequestDto) {

        if (customerRepository.existsByEmailId(registerRequestDto.getEmailId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email ID is already registered as a Customer.");
        }


        if (adminRepository.existsByEmailId(registerRequestDto.getEmailId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email ID is already registered as an Admin.");
        }

        return userService.registerCustomer(registerRequestDto);
    }

    @PostMapping("/login") //basil george
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDTO) {
        try
        {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken
                            (       loginRequestDTO.getUsername(),
                                    loginRequestDTO.getPassword()
                            )
            );
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String role = "CUSTOMER";
            if (!authorities.isEmpty())
            {
                role = authorities.iterator().next().getAuthority();
            }
            String dashboard = role.equals("ROLE_ADMIN") ? "Admin Dashboard" : "User Dashboard";
            return ResponseEntity.ok("Login successful! Redirecting to: " + dashboard);
        }
        catch(Exception e)
        {
            return ResponseEntity.status(401).body("Invalid Credentials");
        }
    }

    @GetMapping("/admin/allAdmins") // shivaramakrishna
    public ResponseEntity<List<Admins>> getAllAdmins() {
        List<Admins> admins = adminRepository.findAll();
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/admin/allCustomers") // shivaramakrishna
    public ResponseEntity<List<Customers>> getAllCustomers() {
        List<Customers> customers = customerRepository.findAll();
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/admin/update/{uid}")
    @PreAuthorize("hasRole('ADMIN')") // rajesh
    public ResponseEntity<String> updateAdmin(@PathVariable String uid, @RequestBody RegisterRequestDto registerRequestDto) {
        return userService.updateAdmin(uid, registerRequestDto);
    }

    @PutMapping("/customer/update/{uid}")
    @PreAuthorize("hasRole('ADMIN')") // rajesh
    public ResponseEntity<String> updateCustomer(@PathVariable String uid, @RequestBody RegisterRequestDto registerRequestDto) {
        return userService.updateCustomer(uid, registerRequestDto);
    }

    @DeleteMapping("/admin/delete/{uid}")
    @PreAuthorize("hasRole('ADMIN')") // basil george
    public ResponseEntity<String> deleteAdmin(@PathVariable String uid) {
        return userService.deleteAdmin(uid);
    }

    @DeleteMapping("/customer/delete/{uid}")
    @PreAuthorize("hasRole('ADMIN')") // basil george
    public ResponseEntity<String> deleteCustomer(@PathVariable String uid) {
        return userService.deleteCustomer(uid);
    }

    @GetMapping("/admin/{uid}")
    @PreAuthorize("hasRole('ADMIN')") // shivaramakrishna
    public ResponseEntity<Admins> getAdminById(@PathVariable String uid) {
        return userService.getAdminById(uid);
    }

    @GetMapping("/customer/{uid}")
    @PreAuthorize("hasRole('ADMIN')")  // shivaramakrishna
    public ResponseEntity<Customers> getCustomerById(@PathVariable String uid) {
        return userService.getCustomerById(uid);
    }

    @GetMapping("/admin/getAdminByEmail/{email}") // shivaramakrishna
    public ResponseEntity<Admins> getAdminByEmail(@PathVariable String email) {
        return userService.getAdminByEmail(email);
    }

    @GetMapping("/customer/getCustomerByEmail/{email}") // shivaramakrishna
    public ResponseEntity<Customers> getCustomerByEmail(@PathVariable String email) {
        return userService.getCustomerByEmail(email);
    }

}