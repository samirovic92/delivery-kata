package com.carrefour.deliverykata.domain.order.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public record CreatOrderCommand(
        @TargetAggregateIdentifier
        UUID orderId,
        Long timeSlotId,
        UUID userId
){}
