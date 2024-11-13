package com.achievers.UserAuthentication.controller;

import com.achievers.UserAuthentication.dto.LoginRequestDto;
import com.achievers.UserAuthentication.dto.RegisterRequestDto;
import com.achievers.UserAuthentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

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
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
            );

            String role = authentication.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .findFirst()
                    .orElse("CUSTOMER");

            String dashboard = role.equals("ROLE_ADMIN") ? "Admin Dashboard" : "User Dashboard";

            return ResponseEntity.ok("Login successful! Redirecting to: " + dashboard);

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: Invalid credentials");
        }
    }
}
