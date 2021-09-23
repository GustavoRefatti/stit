package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class User {

    private String userId;

    private String email;

    private String password;

    private List<String> roles;

}
