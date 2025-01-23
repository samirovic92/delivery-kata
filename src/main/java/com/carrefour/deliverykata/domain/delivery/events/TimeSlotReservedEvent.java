package com.carrefour.deliverykata.domain.delivery.events;

public record TimeSlotReservedEvent(
        Long timeSlotId,
        boolean reserved
){}