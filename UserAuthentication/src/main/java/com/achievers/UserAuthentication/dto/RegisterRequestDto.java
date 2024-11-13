package com.achievers.UserAuthentication.dto;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String fullName;
    private String address;
    private String emailId;
    private String mobileNumber;
    private Integer age;
    private String gender;
    private String password;
}
