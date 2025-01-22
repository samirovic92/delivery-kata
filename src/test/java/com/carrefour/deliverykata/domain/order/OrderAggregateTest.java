package com.carrefour.deliverykata.domain.order;

import com.carrefour.deliverykata.domain.order.commands.CreatOrderCommand;
import com.carrefour.deliverykata.domain.order.events.OrderCreatedEvent;
import com.carrefour.deliverykata.domain.order.models.OrderStatus;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class OrderAggregateTest {

    private AggregateTestFixture<OrderAggregate> fixture;

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(OrderAggregate.class);
    }

    @Test
    public void should_create_order() {
        var orderId = UUID.randomUUID();
        var timeSlotId = 1L;
        var userId = UUID.randomUUID();
        fixture.givenNoPriorActivity()
                .when(new CreatOrderCommand(orderId, timeSlotId, userId))
                .expectEvents(new OrderCreatedEvent(orderId, timeSlotId, userId, OrderStatus.CREATED));

    }

}