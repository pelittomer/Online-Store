package com.online_store.backend.api.product.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public String addProduct(@RequestBody String productDetails) { // Likely a ProductDto
        // This function adds a new product to the system.
        // 'productDetails' would contain all the necessary information for the new
        // product.
        return "New product added: " + productDetails;
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable String id, @RequestBody String updatedProductDetails) { // Likely a
                                                                                                      // ProductDto
        // This function updates the information for an existing product identified by
        // its ID.
        // 'updatedProductDetails' contains the new data for the product.
        return "Product with ID " + id + " updated: " + updatedProductDetails;
    }

    @GetMapping
    public String listProducts() {
        // This function retrieves and lists all available products in the system.
        // It provides a comprehensive list of all products.
        return "List of products will be returned here.";
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable String id) {
        // This function retrieves the detailed information for a single product by its
        // unique ID.
        // It provides all details associated with that specific product.
        return "Details for product with ID " + id + " will be returned here.";
    }
}
