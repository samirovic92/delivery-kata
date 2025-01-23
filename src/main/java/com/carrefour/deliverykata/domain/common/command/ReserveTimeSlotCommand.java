package com.carrefour.deliverykata.domain.common.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record ReserveTimeSlotCommand(
        @TargetAggregateIdentifier
        Long timeSlotId
) {
}
