package com.carrefour.deliverykata.api.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateOrderRequest {
    private Long timeSlotId;
    private UUID userId;

}
