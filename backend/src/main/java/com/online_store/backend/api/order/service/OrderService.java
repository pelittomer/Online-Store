package com.online_store.backend.api.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_store.backend.api.address.entities.Address;
import com.online_store.backend.api.address.utils.AddressUtilsService;
import com.online_store.backend.api.cart.entities.Cart;
import com.online_store.backend.api.cart.utils.CartUtilsService;
import com.online_store.backend.api.order.dto.request.OrderRequestDto;
import com.online_store.backend.api.order.dto.response.OrderDetailsResponseDto;
import com.online_store.backend.api.order.dto.response.OrderResponseDto;
import com.online_store.backend.api.order.entities.Order;
import com.online_store.backend.api.order.repository.OrderRepository;
import com.online_store.backend.api.order.utils.mapper.GetOrderDetailsMapper;
import com.online_store.backend.api.order.utils.mapper.GetOrderMapper;
import com.online_store.backend.api.order.utils.mapper.OrderMapper;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing customer orders.
 * This service handles the creation of new orders from a user's cart,
 * and provides methods to list and view details of existing orders.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    // repositories
    private final OrderRepository orderRepository;
    // utils
    private final CartUtilsService cartUtilsService;
    private final AddressUtilsService addressUtilsService;
    private final CommonUtilsService commonUtilsService;
    // mappers
    private final OrderMapper orderMapper;
    private final GetOrderMapper getOrderMapper;
    private final GetOrderDetailsMapper getOrderDetailsMapper;

    /**
     * Creates a new order for the current user based on their shopping cart.
     * The method first validates that the cart is not empty, then creates the order
     * and finally clears the cart.
     *
     * @param dto The DTO containing the address ID for the order.
     * @return A success message upon successful order creation.
     * @see com.online_store.backend.api.order.controller.OrderController#createOrder(OrderRequestDto)
     */
    @Transactional
    public String createOrder(OrderRequestDto dto) {
        User user = commonUtilsService.getCurrentUser();
        log.info("Creating a new order for user: {}", user.getEmail());

        Address address = addressUtilsService.findAddressById(dto.getAddress());
        Cart cart = cartUtilsService.getCartByUser(user);
        if (cart.getCartItems().size() == 0) {
            log.warn("Order creation failed for user {} due to empty cart.", user.getEmail());
            throw new Error("Cart items not found!");
        }
        Order order = orderMapper.createOrderMapper(cart, address);
        orderRepository.save(order);
        cartUtilsService.clearCartItems(cart);

        log.info("Order ID: {} created successfully for user: {}", order.getId(), user.getEmail());
        return "Order created succesfully.";
    }

    /**
     * Retrieves a list of all orders for the current authenticated user.
     *
     * @return A list of {@link OrderResponseDto} objects, each representing an
     *         order.
     * @see com.online_store.backend.api.order.controller.OrderController#listUserOrders()
     */
    public List<OrderResponseDto> listUserOrders() {
        User user = commonUtilsService.getCurrentUser();
        log.info("Listing all orders for user: {}", user.getEmail());

        List<Order> order = orderRepository.findByUser(user);
        return order.stream()
                .map(getOrderMapper::orderResponseMapper).toList();
    }

    /**
     * Retrieves the detailed information for a specific order.
     * It ensures that the order belongs to the current user for security purposes.
     *
     * @param id The ID of the order to retrieve.
     * @return An {@link OrderDetailsResponseDto} containing all details of the
     *         order.
     * @throws EntityNotFoundException if the order with the given ID does not
     *                                 exist.
     * @throws Error                   if the current user is not the owner of the
     *                                 order.
     * @see com.online_store.backend.api.order.controller.OrderController#getOrderDetails(Long)
     */
    public OrderDetailsResponseDto getOrderDetails(Long id) {
        User currentUser = commonUtilsService.getCurrentUser();
        log.info("Fetching order details for order ID: {} for user: {}", id, currentUser.getEmail());

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));

        if (!order.getUser().equals(currentUser)) {
            throw new Error("You are not authorized to view this order.");
        }

        return getOrderDetailsMapper.orderDetailsMapper(order);
    }

}
