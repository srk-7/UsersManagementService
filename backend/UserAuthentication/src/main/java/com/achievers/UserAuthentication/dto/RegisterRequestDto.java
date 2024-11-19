package com.achievers.UserAuthentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// basil george
@Data
@NoArgsConstructor
@AllArgsConstructor

public class RegisterRequestDto {
    private String fullName;
    private String address;
    private String emailId;
    private String mobileNumber;
    private Integer age;
    private String gender;
    private String password;

}
