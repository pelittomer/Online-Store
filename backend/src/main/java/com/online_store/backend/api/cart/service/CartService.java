package com.online_store.backend.api.cart.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.cart.dto.request.CartRequestDto;
import com.online_store.backend.api.cart.dto.request.UpdateCartRequestDto;
import com.online_store.backend.api.cart.dto.response.CartResponseDto;
import com.online_store.backend.api.cart.entities.Cart;
import com.online_store.backend.api.cart.entities.CartItem;
import com.online_store.backend.api.cart.repository.CartItemRepository;
import com.online_store.backend.api.cart.repository.CartRepository;
import com.online_store.backend.api.cart.utils.CartUtilsService;
import com.online_store.backend.api.cart.utils.mapper.CreatCartMapper;
import com.online_store.backend.api.cart.utils.mapper.GetCartMapper;
import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.product.entities.ProductStock;
import com.online_store.backend.api.product.utils.ProductUtilsService;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing shopping carts.
 * Provides functionality to add, remove, update, and list items in a user's
 * cart.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
        // repositories
        private final CartRepository cartRepository;
        private final CartItemRepository cartItemRepository;
        // utils
        private final ProductUtilsService productUtilsService;
        private final CommonUtilsService commonUtilsService;
        private final CartUtilsService cartUtilsService;
        // mappers
        private final GetCartMapper getCartMapper;
        private final CreatCartMapper creatCartMapper;

        /**
         * Adds a product to the current user's shopping cart.
         * If the product already exists in the cart, its quantity is updated.
         * If not, a new cart item is created.
         *
         * @param dto The DTO containing the product, product stock, and quantity to
         *            add.
         * @return A message indicating whether the product was added or the quantity
         *         was updated.
         * @see com.online_store.backend.api.cart.controller.CartController#addProductToCart(CartRequestDto)
         */
        public String addProductToCart(CartRequestDto dto) {
                log.info("Attempting to add product ID: {} to cart for current user.", dto.getProduct());
                User user = commonUtilsService.getCurrentUser();

                Product product = productUtilsService.findProductById(dto.getProduct());

                ProductStock productStock = product.getProductStocks().stream()
                                .filter(stock -> stock.getId().equals(dto.getProductStock()))
                                .findFirst()
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Product stock not found for the given product!"));

                Cart cart = cartRepository.findByUser(user)
                                .orElseGet(() -> {
                                        Cart newCart = creatCartMapper.cartMapper(user);
                                        return cartRepository.save(newCart);
                                });

                Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                                .filter(item -> item.getProductStock().getId().equals(productStock.getId()))
                                .findFirst();

                if (existingCartItem.isPresent()) {
                        CartItem cartItem = existingCartItem.get();
                        cartItem.setQuantity(cartItem.getQuantity() + dto.getQuantity());
                        cartItemRepository.save(cartItem);
                        return "Product quantity updated in cart.";
                } else {
                        CartItem newCartItem = creatCartMapper.cartItemMapper(cart,
                                        productStock,
                                        product,
                                        dto.getQuantity());

                        cart.getCartItems().add(newCartItem);
                        cartRepository.save(cart);

                        return "Product added to cart.";
                }
        }

        /**
         * Removes a specific cart item from the current user's cart.
         *
         * @param cartItemId The ID of the cart item to remove.
         * @return A success message.
         * @throws EntityNotFoundException if the cart item with the given ID is not
         *                                 found in the user's cart.
         * @see com.online_store.backend.api.cart.controller.CartController#removeProductFromCart(Long)
         */
        public String removeProductFromCart(Long cartItemId) {
                log.info("Attempting to remove cart item with ID: {} from cart.", cartItemId);
                User user = commonUtilsService.getCurrentUser();

                Cart cart = cartUtilsService.getCartByUser(user);

                CartItem cartItemToRemove = cart.getCartItems().stream()
                                .filter(item -> item.getId().equals(cartItemId))
                                .findFirst()
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Cart item not found with ID: " + cartItemId));

                cart.getCartItems().remove(cartItemToRemove);
                cartRepository.save(cart);
                cartItemRepository.delete(cartItemToRemove);

                log.info("Cart item with ID: {} removed from cart for user: {}", cartItemId, user.getEmail());
                return "Product removed from cart successfully.";
        }

        /**
         * Retrieves the current user's shopping cart details.
         *
         * @return A {@link CartResponseDto} representing the user's cart and its items.
         * @see com.online_store.backend.api.cart.controller.CartController#listCartItems()
         */
        public CartResponseDto listCartItems() {
                log.info("Listing cart items for current user.");
                User user = commonUtilsService.getCurrentUser();
                Cart cart = cartUtilsService.getCartByUser(user);
                return getCartMapper.cartMapper(cart);
        }

        /**
         * Clears all items from the current user's shopping cart.
         *
         * @return A success message.
         * @see com.online_store.backend.api.cart.controller.CartController#clearAllCartItems()
         */
        public String clearAllCartItems() {
                log.info("Attempting to clear all cart items for current user.");
                User user = commonUtilsService.getCurrentUser();

                Cart cart = cartUtilsService.getCartByUser(user);
                cart.getCartItems().clear();
                cartRepository.save(cart);

                log.info("All cart items cleared successfully for user: {}", user.getEmail());
                return "Cart clear.";
        }

        /**
         * Updates the quantity of a specific item in the current user's cart.
         *
         * @param dto The DTO containing the cart item ID and the new quantity.
         * @return A success message.
         * @throws EntityNotFoundException if the cart item is not found.
         * @see com.online_store.backend.api.cart.controller.CartController#updateCartItem(UpdateCartRequestDto)
         */
        public String updateCartItem(UpdateCartRequestDto dto) {
                log.info("Attempting to update cart item with ID: {} for user.", dto.getId());
                User user = commonUtilsService.getCurrentUser();

                Cart cart = cartUtilsService.getCartByUser(user);

                CartItem cartItemToUpdate = cart.getCartItems().stream()
                                .filter(item -> item.getId().equals(dto.getId()))
                                .findFirst()
                                .orElseThrow(
                                                () -> new EntityNotFoundException(
                                                                "Cart item not found in your cart with ID: "
                                                                                + dto.getId()));

                if (dto.getQuantity() <= 0) {
                        log.warn("Invalid quantity update attempt for cart item ID: {}. Quantity: {}", dto.getId(),
                                        dto.getQuantity());
                        return "Quantity must be a positive number.";
                }

                cartItemToUpdate.setQuantity(dto.getQuantity());
                cartItemRepository.save(cartItemToUpdate);

                log.info("Cart item with ID: {} updated successfully to quantity: {}", dto.getId(), dto.getQuantity());
                return "Cart item updated successfully.";
        }

}
