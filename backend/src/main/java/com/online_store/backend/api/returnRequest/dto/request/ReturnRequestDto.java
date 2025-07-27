package com.online_store.backend.api.returnRequest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRequestDto {
    @NotBlank(message = "Reason cannot be blank.")
    @Size(min = 10, max = 500, message = "Reason must be between 10 and 500 characters.")
    private String reason;

    @NotNull(message = "Quantity cannot be null.")
    @Min(value = 1, message = "Quantity must be at least 1.")
    private Integer quantity;

    @NotNull(message = "Order item ID cannot be null.")
    @Min(value = 1, message = "Order item ID must be a positive number.")
    private Long orderItem;
}
