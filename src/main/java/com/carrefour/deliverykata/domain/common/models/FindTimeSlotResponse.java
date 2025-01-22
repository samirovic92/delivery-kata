package com.carrefour.deliverykata.domain.common.models;

public record FindTimeSlotResponse(
        Long timeSlotId,
        Boolean reserved
) {
}
