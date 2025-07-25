package com.online_store.backend.api.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_store.backend.api.address.entities.Address;
import com.online_store.backend.api.address.repository.AddressRepository;
import com.online_store.backend.api.cart.entities.Cart;
import com.online_store.backend.api.cart.repository.CartRepository;
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

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CommonUtilsService commonUtilsService;
    private final OrderMapper orderMapper;
    private final GetOrderMapper getOrderMapper;
    private final GetOrderDetailsMapper getOrderDetailsMapper;
    private final AddressRepository addressRepository;

    @Transactional
    public String createOrder(OrderRequestDto dto) {
        User user = commonUtilsService.getCurrentUser();
        Address address = addressRepository.findById(dto.getAddress())
                .orElseThrow(() -> new EntityNotFoundException("Addres not found!"));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException(""));
        if (cart.getCartItems().size() == 0) {
            throw new Error("Cart items not found!");
        }
        Order order = orderMapper.createOrderMapper(cart, address);

        orderRepository.save(order);
        return "Order created succesfully.";
    }

    public List<OrderResponseDto> listUserOrders() {
        User user = commonUtilsService.getCurrentUser();
        List<Order> order = orderRepository.findByUser(user);
        return order.stream()
                .map(getOrderMapper::orderResponseMapper).toList();
    }

    public OrderDetailsResponseDto getOrderDetails(Long id) {
        User currentUser = commonUtilsService.getCurrentUser();

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));

        if (!order.getUser().equals(currentUser)) {
            throw new Error("You are not authorized to view this order.");
        }

        return getOrderDetailsMapper.orderDetailsMapper(order);
    }

}
