package com.carrefour.deliverykata.infrastructure.order;

import com.carrefour.deliverykata.domain.order.models.Order;
import com.carrefour.deliverykata.domain.order.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderAdapter implements OrderRepository {
    private final JpaOrderRepository jpaOrderRepository;

    public OrderAdapter(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public void creatOrder(Order order) {
        var orderEntity = new OrderEntity(
                order.getOrderId(),
                order.getTimeSlotId(),
                order.getUserId(),
                order.getOrderStatus()
        );
        jpaOrderRepository.save(orderEntity);
    }
}
