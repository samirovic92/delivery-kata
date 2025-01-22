package com.carrefour.deliverykata.domain.order.exceptions;

public class OrderException extends RuntimeException{

    public OrderException(String message) {
        super(message);
    }
}
