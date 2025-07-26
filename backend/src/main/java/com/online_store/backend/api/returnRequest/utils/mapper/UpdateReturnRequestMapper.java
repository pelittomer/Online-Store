package com.online_store.backend.api.returnRequest.utils.mapper;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.order.entities.OrderStatus;
import com.online_store.backend.api.returnRequest.dto.request.UpdateReturnRequestDto;
import com.online_store.backend.api.returnRequest.entities.ReturnRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateReturnRequestMapper {

    public ReturnRequest returnRequestMapper(ReturnRequest returnRequest,
            UpdateReturnRequestDto dto) {
        returnRequest.setStatus(dto.getStatus());
        if (dto.getRejectionReason() != null) {
            returnRequest.setRejectionReason(dto.getRejectionReason());
        } else {
            returnRequest.setRejectionReason(null);
        }
        if (dto.getStatus().equals(OrderStatus.RETURN_APPROVED)) {
            returnRequest.setReturnCode(generateReturnCode());
        } else {
            returnRequest.setReturnCode(null);
        }
        return returnRequest;
    }

    private String generateReturnCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return "RET-" + sb.toString();
    }
}
