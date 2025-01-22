package com.carrefour.deliverykata.domain.order.events;

import com.carrefour.deliverykata.domain.order.models.Order;
import com.carrefour.deliverykata.domain.order.models.OrderStatus;

import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,
        Long timeSlotId,
        UUID userId,
        OrderStatus orderStatus
) {

    public Order toDomain() {
        return new Order(
                this.orderId,
                this.timeSlotId,
                this.userId,
                this.orderStatus
        );
    }
}
