package com.example.demo.repository;

import com.example.demo.domain.Products;
import com.example.demo.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ProductsRepository {

    @Value("classpath:fixtures/products.txt")
    Resource resourceFile;

    private List<Products> products;

    public List<Products> getProducts() {
        if (products == null) {
            products = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();
            Stream<String> lines = null;
            try {
                lines = Files.lines(resourceFile.getFile().toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            lines.forEach(line -> {
                try {
                    products.add(mapper.readValue(line, Products.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        }
        return products;
    }


}
