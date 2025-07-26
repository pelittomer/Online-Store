package com.online_store.backend.api.returnRequest.dto.request;

import com.online_store.backend.api.order.entities.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReturnRequestDto {
    private OrderStatus status;
    private String rejectionReason;
}
