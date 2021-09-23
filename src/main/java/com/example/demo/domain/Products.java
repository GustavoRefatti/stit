package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Products {
    private String name;

    private String department;

    private String material;

    private String price;

    private List<String> tags;

}
