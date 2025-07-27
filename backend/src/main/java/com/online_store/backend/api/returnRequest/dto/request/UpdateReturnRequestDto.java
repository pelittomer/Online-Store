package com.online_store.backend.api.returnRequest.dto.request;

import com.online_store.backend.api.order.entities.OrderStatus;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReturnRequestDto {
    @NotNull(message = "Status cannot be null.")
    private OrderStatus status;
    
    private String rejectionReason;

    @AssertTrue(message = "Rejection reason must be provided when the status is 'REJECTED'.")
    private boolean isRejectionReasonProvided() {
        if (status == OrderStatus.RETURN_REJECTED) {
            return rejectionReason != null && !rejectionReason.isBlank();
        }
        return true;
    }
}
