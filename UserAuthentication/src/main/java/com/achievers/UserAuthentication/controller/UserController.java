package com.achievers.UserAuthentication.controller;

import com.achievers.UserAuthentication.dto.LoginRequestDto;
import com.achievers.UserAuthentication.dto.RegisterRequestDto;
import com.achievers.UserAuthentication.model.Admins;
import com.achievers.UserAuthentication.model.Customers;
import com.achievers.UserAuthentication.repository.AdminRepository;
import com.achievers.UserAuthentication.repository.CustomerRepository;
import com.achievers.UserAuthentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterRequestDto registerRequestDto) {
        return userService.registerAdmin(registerRequestDto);
    }

    @PostMapping("/register/customer")
    public ResponseEntity<String> registerCustomer(@RequestBody RegisterRequestDto registerRequestDto) {
        return userService.registerCustomer(registerRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
        );

        String role = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .findFirst()
                .orElse("CUSTOMER");

        String dashboard = role.equals("ROLE_ADMIN") ? "Admin Dashboard" : "User Dashboard";

        return ResponseEntity.ok("Login successful! Redirecting to: " + dashboard);
    }

    @GetMapping("/admin/allAdmins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Admins>> getAllAdmins() {
        List<Admins> admins = adminRepository.findAll();
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/admin/allCustomers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Customers>> getAllCustomers() {
        List<Customers> customers = customerRepository.findAll();
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/admin/update/{uid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateAdmin(@PathVariable String uid, @RequestBody RegisterRequestDto registerRequestDto) {
        return userService.updateAdmin(uid, registerRequestDto);
    }

    @PutMapping("/customer/update/{uid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateCustomer(@PathVariable String uid, @RequestBody RegisterRequestDto registerRequestDto) {
        return userService.updateCustomer(uid, registerRequestDto);
    }

    @DeleteMapping("/admin/delete/{uid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAdmin(@PathVariable String uid) {
        return userService.deleteAdmin(uid);
    }

    @DeleteMapping("/customer/delete/{uid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCustomer(@PathVariable String uid) {
        return userService.deleteCustomer(uid);
    }

    @GetMapping("/admin/{uid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Admins> getAdminById(@PathVariable String uid) {
        return userService.getAdminById(uid);
    }

    @GetMapping("/customer/{uid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Customers> getCustomerById(@PathVariable String uid) {
        return userService.getCustomerById(uid);
    }
}