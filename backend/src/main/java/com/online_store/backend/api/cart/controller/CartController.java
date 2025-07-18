package com.online_store.backend.api.cart.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.cart.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public String addProductToCart(@RequestBody String productId) {
        // This function adds a product to the user's shopping cart.
        // The 'productId' (or full product details) is expected in the request body.
        return "Product with ID " + productId + " added to cart.";
    }

    @DeleteMapping("/{id}")
    public String removeProductFromCart(@PathVariable String id) {
        // This function removes a specific product from the user's shopping cart by its
        // ID.
        // The 'id' is extracted from the URL path.
        return "Product with ID " + id + " removed from cart.";
    }

    @DeleteMapping("/all")
    public String clearAllCartItems() {
        // This function removes all products from the user's shopping cart.
        // It clears the entire cart for the current user.
        return "All items cleared from the cart.";
    }

    @GetMapping
    public String listCartItems() {
        // This function retrieves and lists all products currently in the user's
        // shopping cart.
        // It returns the current contents of the user's cart.
        return "List of products in the cart will be returned here.";
    }

    @PutMapping("/{id}")
    public String updateCartItem(@PathVariable String id, @RequestBody String updatedQuantity) {
        // This function updates the details of a specific product in the cart,
        // identified by its ID.
        // This often includes updating the quantity of the product.
        return "Cart item with ID " + id + " updated with details: " + updatedQuantity;
    }
}
