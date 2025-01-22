package com.carrefour.deliverykata.domain.order;

import com.carrefour.deliverykata.domain.order.models.Order;

public interface OrderRepository {

    void creatOrder(Order order);
}
