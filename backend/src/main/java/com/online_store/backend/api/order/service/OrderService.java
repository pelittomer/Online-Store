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

    public List<OrderResponseDto> listUserOrders() {
        User user = commonUtilsService.getCurrentUser();
        log.info("Listing all orders for user: {}", user.getEmail());

        List<Order> order = orderRepository.findByUser(user);
        return order.stream()
                .map(getOrderMapper::orderResponseMapper).toList();
    }

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
