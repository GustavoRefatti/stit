package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
public class Role implements GrantedAuthority {

    String authority;

}
