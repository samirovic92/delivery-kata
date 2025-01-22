package com.carrefour.deliverykata.infrastructure.order;

import com.carrefour.deliverykata.domain.order.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static com.carrefour.deliverykata.domain.order.OrderStatus.CREATED;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class OrderAdapterTest {
    @Autowired
    JpaOrderRepository jpaOrderRepository;
    OrderAdapter orderAdapter;

    @BeforeEach
    public void setup() {
        orderAdapter = new OrderAdapter(jpaOrderRepository);
    }

    @Test
    public void should_create_new_order() {
        // Given
        var orderId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var order = new Order(orderId, 1L, userId, CREATED);
        var orderCreated = new OrderEntity(orderId, 1L, userId, CREATED);

        // When
        orderAdapter.creatOrder(order);

        // Then
        assertThat(jpaOrderRepository.findById(orderId)).isEqualTo(Optional.of(orderCreated));
    }
}