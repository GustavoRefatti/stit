package com.example.demo.repository;

import com.example.demo.domain.User;
import com.example.demo.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class UserRepository {

    @Value("classpath:fixtures/users.json")
    Resource resourceFile;

    private List<User> users;

    private List<User> getUsers() {
        if (users == null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                users = Arrays.asList(mapper.readValue(resourceFile.getFile(), User[].class));
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }

        return users;
    }

    public User getUserByEmail(String email) {
       return getUsers().stream().filter(u -> u.getEmail().equals(email)).findFirst().orElseThrow(() ->
                new NotFoundException("user not found"));
    }
}