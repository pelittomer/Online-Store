package com.online_store.backend.api.cart.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.cart.entities.Cart;
import com.online_store.backend.api.cart.repository.CartRepository;
import com.online_store.backend.api.user.entities.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartUtilsService {
    //repositories
    private final CartRepository cartRepository;

    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for the current user."));
    }

    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    log.info("Cart not found for user: {}. Creating a new one.", user.getEmail());
                    return cartRepository.save(Cart.builder().user(user).build());
                });
    }
}
