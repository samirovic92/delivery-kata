package com.carrefour.deliverykata.api;

import com.carrefour.deliverykata.api.Dto.CreateOrderRequest;
import com.carrefour.deliverykata.domain.order.commands.CreatOrderCommand;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final CommandGateway commandGateway;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UUID> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        var creatOrderCommand = new CreatOrderCommand(
                UUID.randomUUID(),
                createOrderRequest.getTimeSlotId(),
                createOrderRequest.getUserId()
        );
        var orderId = commandGateway.sendAndWait(creatOrderCommand);

        return Mono.just((UUID) orderId);
    }

}
