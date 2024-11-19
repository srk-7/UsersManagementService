package com.achievers.UserAuthentication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
// rajesh
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "admins")
public class Admins {

    @Id
    private String aid;
    private String uid;
    private String fullName;
    private String address;
    private String emailId;
    private String mobileNumber;
    private Integer age;
    private String gender;
}
