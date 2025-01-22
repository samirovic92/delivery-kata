package com.carrefour.deliverykata.infrastructure.order;

import com.carrefour.deliverykata.domain.order.models.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Orders")
public class OrderEntity {

    @Id
    private UUID orderId;
    private Long timeSlotId;
    private UUID userId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return Objects.equals(orderId, that.orderId);
    }

}
