package com.online_store.backend.api.cart.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.cart.entities.Cart;
import com.online_store.backend.api.cart.repository.CartRepository;
import com.online_store.backend.api.user.entities.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility service for Cart-related operations.
 * This component provides helper methods for retrieving, creating, and managing
 * shopping carts for users, ensuring consistent error handling and logging.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CartUtilsService {
    // repositories
    private final CartRepository cartRepository;

    /**
     * Retrieves the shopping cart for a given user.
     *
     * @param user The user for whom to find the cart.
     * @return The {@link Cart} entity belonging to the user.
     * @throws EntityNotFoundException if no cart is found for the specified user.
     * @see com.online_store.backend.api.cart.service.CartService#updateCartItem(com.online_store.backend.api.cart.dto.request.UpdateCartRequestDto)
     * @see com.online_store.backend.api.cart.service.CartService#clearAllCartItems()
     * @see com.online_store.backend.api.cart.service.CartService#listCartItems()
     * @see com.online_store.backend.api.cart.service.CartService#removeProductFromCart(Long)
     */
    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for the current user."));
    }

    /**
     * Clears all items from a given shopping cart.
     *
     * @param cart The {@link Cart} to clear.
     * @see com.online_store.backend.api.order.service.OrderService#createOrder(com.online_store.backend.api.order.dto.request.OrderRequestDto)
     */
    public void clearCartItems(Cart cart) {
        log.info("Clearing all cart items for user: {}", cart.getUser().getEmail());
        cart.getCartItems().clear();
        cartRepository.save(cart);
        log.info("Cart for user {} cleared successfully.", cart.getUser().getEmail());
    }
}
