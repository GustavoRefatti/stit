package com.example.demo.controller;

import com.example.demo.domain.ProductResponse;
import com.example.demo.domain.Products;
import com.example.demo.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    ProductsRepository productRepository;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ProductResponse getProducts(){
        List<Products> productsList = productRepository.getProducts();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setTotal(productsList.size());
        productResponse.setProducts(productsList);
        return productResponse;
    }

}
