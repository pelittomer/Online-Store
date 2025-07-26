package com.online_store.backend.api.returnRequest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRequestDto {
    private String reason;
    private Integer quantity;
    private Long orderItem;
}
