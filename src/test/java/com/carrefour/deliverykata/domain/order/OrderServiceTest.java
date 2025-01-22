package com.carrefour.deliverykata.domain.order;

import com.carrefour.deliverykata.domain.common.command.ReserveTimeSlotCommand;
import com.carrefour.deliverykata.domain.common.models.FindTimeSlotResponse;
import com.carrefour.deliverykata.domain.common.queries.FindTimeSlotQuery;
import com.carrefour.deliverykata.domain.order.events.OrderCreatedEvent;
import com.carrefour.deliverykata.domain.order.exceptions.OrderException;
import com.carrefour.deliverykata.domain.order.models.Order;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.carrefour.deliverykata.domain.order.models.OrderStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {
    OrderRepository orderRepository = mock();
    QueryGateway queryGateway = mock();
    CommandGateway commandGateway = mock();
    OrderService orderService = new OrderService(orderRepository, queryGateway, commandGateway);

    @Test
    public void should_create_order() {
        // Given
        var orderId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var orderCreatedEvent = new OrderCreatedEvent(orderId,1L, userId, CREATED);
        var order = new Order(orderId, 1L, userId, CREATED);
        var findTimeSlotQuery = new FindTimeSlotQuery(1l);

        when(queryGateway.query(findTimeSlotQuery, FindTimeSlotResponse.class))
                .thenReturn(CompletableFuture.completedFuture(new FindTimeSlotResponse(1L, false)));

        // When
        orderService.createOrder(orderCreatedEvent);

        // then
        verify(orderRepository).creatOrder(order);
        verify(queryGateway, times(1)).query(findTimeSlotQuery, FindTimeSlotResponse.class);
    }

    @Test
    public void should_send_request_to_reserve_time_slot() {
        var orderId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var orderCreatedEvent = new OrderCreatedEvent(orderId,1L, userId, CREATED);
        var reserveTimeSlotCommand = new ReserveTimeSlotCommand(1l);
        var findTimeSlotQuery = new FindTimeSlotQuery(1l);

        when(queryGateway.query(findTimeSlotQuery, FindTimeSlotResponse.class))
                .thenReturn(CompletableFuture.completedFuture(new FindTimeSlotResponse(1L, false)));

        // When
        orderService.createOrder(orderCreatedEvent);

        // then
        verify(commandGateway, times(1)).send(reserveTimeSlotCommand);
    }

    @Test
    public void should_not_create_order_if_timeslot_not_valid() {
        // Given
        var orderId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var orderCreatedEvent = new OrderCreatedEvent(orderId,1L, userId, CREATED);
        var findTimeSlotQuery = new FindTimeSlotQuery(1l);
        var errorMessage = String.format("Error when creating order with id %s, the selected timeslot not exist", orderId);

        when(queryGateway.query(findTimeSlotQuery, FindTimeSlotResponse.class))
                .thenReturn(CompletableFuture.completedFuture(null));

        // When
        var exception = assertThrows(OrderException.class, () -> orderService.createOrder(orderCreatedEvent));

        // Then
        assertThat(exception.getMessage()).isEqualTo(errorMessage);
    }

    @Test
    public void should_not_create_order_if_timeslot_already_reserved() {
        var orderId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var orderCreatedEvent = new OrderCreatedEvent(orderId,1L, userId, CREATED);
        var errorMessage = String.format("Error when creating order with id %s, the timeslot already reserved", orderId);
        var findTimeSlotQuery = new FindTimeSlotQuery(1l);

        when(queryGateway.query(findTimeSlotQuery, FindTimeSlotResponse.class))
                .thenReturn(CompletableFuture.completedFuture(new FindTimeSlotResponse(1L, true)));

        // When
        var exception = assertThrows(OrderException.class, () -> orderService.createOrder(orderCreatedEvent));

        // Then
        assertThat(exception.getMessage()).isEqualTo(errorMessage);
    }
}