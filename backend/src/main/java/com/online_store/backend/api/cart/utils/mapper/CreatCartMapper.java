package com.online_store.backend.api.cart.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.cart.entities.Cart;
import com.online_store.backend.api.cart.entities.CartItem;
import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.product.entities.ProductStock;
import com.online_store.backend.api.user.entities.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreatCartMapper {

    public Cart cartMapper(User user) {
        return Cart.builder()
                .user(user)
                .build();
    }

    public CartItem cartItemMapper(Cart cart,
            ProductStock productStock,
            Product product,
            Integer quantity) {
        return CartItem.builder()
                .cart(cart)
                .productStock(productStock)
                .product(product)
                .quantity(quantity)
                .build();
    }
}
