package com.online_store.backend.api.returnRequest.utils.mapper;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.order.entities.OrderItem;
import com.online_store.backend.api.returnRequest.dto.request.ReturnRequestDto;
import com.online_store.backend.api.returnRequest.entities.ReturnRequest;
import com.online_store.backend.api.user.entities.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateReturnRequestMapper {

    public ReturnRequest returnRequestMapper(ReturnRequestDto dto,
            OrderItem orderItem,
            User user) {
        return ReturnRequest.builder()
                .returnCode(null)
                .reason(dto.getReason())
                .quantity(dto.getQuantity())
                .shippingTrackingCode(null)
                .orderItem(orderItem)
                .build();
    }

}
