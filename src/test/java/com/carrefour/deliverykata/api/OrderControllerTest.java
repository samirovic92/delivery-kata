package com.carrefour.deliverykata.api;

import com.carrefour.deliverykata.api.Dto.CreateOrderRequest;
import com.carrefour.deliverykata.config.TestSecurityConfig;
import com.carrefour.deliverykata.domain.order.commands.CreatOrderCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Import(TestSecurityConfig.class)
@WebFluxTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CommandGateway commandGateway;

    @Test
    @WithMockUser(username = "test-client", roles = {"CLIENT"})
    void test_Create_Order_Success() {
        // Given
        var orderId = UUID.randomUUID();
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, UUID.randomUUID());
        when(commandGateway.sendAndWait(any(CreatOrderCommand.class))).thenReturn(orderId);

        // When + Then
        webTestClient.post()
                .uri("/orders")
                .bodyValue(createOrderRequest)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(UUID.class)
                .value(orderIdResponse -> {
                    assert orderIdResponse != null;
                    assert orderIdResponse.equals(orderId);
                });

        verify(commandGateway, times(1)).sendAndWait(any(CreatOrderCommand.class));
    }
}