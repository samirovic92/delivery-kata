package com.carrefour.deliverykata.domain.order;

import com.carrefour.deliverykata.domain.order.commands.CreatOrderCommand;
import com.carrefour.deliverykata.domain.order.events.OrderCreatedEvent;
import com.carrefour.deliverykata.domain.order.models.OrderStatus;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

@NoArgsConstructor
@Aggregate
public class OrderAggregate {
    @AggregateIdentifier
    public UUID orderId;
    private Long timeSlotId;
    private UUID userId;
    private OrderStatus orderStatus;


    @CommandHandler
    public OrderAggregate(CreatOrderCommand creatOrderCommand) {
        var orderCreatedEvent = new OrderCreatedEvent(
                creatOrderCommand.orderId(),
                creatOrderCommand.timeSlotId(),
                creatOrderCommand.userId(),
                OrderStatus.CREATED
        );
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        this.orderId = orderCreatedEvent.orderId();
        this.timeSlotId = orderCreatedEvent.timeSlotId();
        this.userId = orderCreatedEvent.userId();
        this.orderStatus = orderCreatedEvent.orderStatus();
    }
}
