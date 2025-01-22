package com.carrefour.deliverykata.domain.order;

import com.carrefour.deliverykata.domain.common.command.ReserveTimeSlotCommand;
import com.carrefour.deliverykata.domain.common.models.FindTimeSlotResponse;
import com.carrefour.deliverykata.domain.common.queries.FindTimeSlotQuery;
import com.carrefour.deliverykata.domain.order.events.OrderCreatedEvent;
import com.carrefour.deliverykata.domain.order.exceptions.OrderException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import java.util.Objects;

@ProcessingGroup("order-group")
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final QueryGateway queryGateway;
    private final CommandGateway commandGateway;

    public OrderService(OrderRepository orderRepository, QueryGateway queryGateway, CommandGateway commandGateway) {
        this.orderRepository = orderRepository;
        this.queryGateway = queryGateway;
        this.commandGateway = commandGateway;
    }

    @EventHandler
    public void createOrder(OrderCreatedEvent event) {
        var timeSlot = queryGateway.query(new FindTimeSlotQuery(event.timeSlotId()), FindTimeSlotResponse.class).join();
        if(Objects.isNull(timeSlot)) {
            throw  new OrderException(String.format("Error when creating order with id %s, the selected timeslot not exist", event.orderId()));
        }
        if(timeSlot.reserved()) {
            throw  new OrderException(String.format("Error when creating order with id %s, the timeslot already reserved", event.orderId()));
        }
        commandGateway.send(new ReserveTimeSlotCommand(event.timeSlotId()));
        orderRepository.creatOrder(event.toDomain());
    }
}
