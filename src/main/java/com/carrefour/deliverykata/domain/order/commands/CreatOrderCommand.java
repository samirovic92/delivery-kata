package com.carrefour.deliverykata.domain.order.commands;

import java.util.UUID;

public record CreatOrderCommand(
        UUID orderId,
        Long timeSlotId,
        UUID userId
){}
