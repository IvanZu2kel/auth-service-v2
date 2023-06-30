package com.example.authservice.api;

import com.example.authservice.config.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Person {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private Role roles;
}
