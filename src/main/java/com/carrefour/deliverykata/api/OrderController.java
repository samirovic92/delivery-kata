package com.carrefour.deliverykata.api;

import com.carrefour.deliverykata.api.Dto.CreateOrderRequest;
import com.carrefour.deliverykata.domain.order.commands.CreatOrderCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
@Tag(name = "Order Management", description = "Endpoints for managing orders")
public class OrderController {
    private final CommandGateway commandGateway;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "create order", description = "create order")
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
