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
import com.online_store.backend.api.cart.utils.mapper.GetCartMapper;
import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.product.entities.ProductStock;
import com.online_store.backend.api.product.repository.ProductRepository;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CommonUtilsService commonUtilsService;
    private final GetCartMapper getCartMapper;

    public String addProductToCart(CartRequestDto cartRequestDto) {
        User user = commonUtilsService.getCurrentUser();

        Product product = productRepository.findById(cartRequestDto.getProduct())
                .orElseThrow(() -> new EntityNotFoundException("Product not found!"));

        ProductStock productStock = product.getProductStocks().stream()
                .filter(stock -> stock.getId().equals(cartRequestDto.getProductStock()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Product stock not found for the given product!"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().user(user).build();
                    return cartRepository.save(newCart);
                });

        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProductStock().getId().equals(productStock.getId()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartRequestDto.getQuantity());
            cartItemRepository.save(cartItem);
            return "Product quantity updated in cart.";
        } else {
            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
                    .productStock(productStock)
                    .product(product)
                    .quantity(cartRequestDto.getQuantity())
                    .build();

            cart.getCartItems().add(newCartItem);
            cartRepository.save(cart);

            return "Product added to cart.";
        }
    }

    public String removeProductFromCart(Long cartItemId) {
        User user = commonUtilsService.getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for the current user."));

        CartItem cartItemToRemove = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found with ID: " + cartItemId));

        cart.getCartItems().remove(cartItemToRemove);

        cartRepository.save(cart);
        cartItemRepository.delete(cartItemToRemove);

        return "Product removed from cart successfully.";
    }

    public CartResponseDto listCartItems() {
        User user = commonUtilsService.getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found!"));

        return getCartMapper.cartMapper(cart);
    }

    public String clearAllCartItems() {
        User user = commonUtilsService.getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found!"));
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return "Cart clear.";
    }

    public String updateCartItem(UpdateCartRequestDto dto) {
        User user = commonUtilsService.getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for the current user."));

        CartItem cartItemToUpdate = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(dto.getId()))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Cart item not found in your cart with ID: " + dto.getId()));

        if (dto.getQuantity() <= 0) {
            return "Quantity must be a positive number.";
        }

        cartItemToUpdate.setQuantity(dto.getQuantity());

        cartItemRepository.save(cartItemToUpdate);

        return "Cart item updated successfully.";
    }

}
