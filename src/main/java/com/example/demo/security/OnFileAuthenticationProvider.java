package com.example.demo.security;

import com.example.demo.domain.Role;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OnFileAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        com.example.demo.domain.User user = userRepository.getUserByEmail(login);

        if (user.getPassword().equals(password)) {
            List<Role> userRoles = user.getRoles().stream().map(role -> new Role(role)).collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(new User(user.getUserId(), password, userRoles), password, userRoles);
        }

        throw new BadCredentialsException("Username or password invalids");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


}
