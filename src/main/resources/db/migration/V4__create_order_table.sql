CREATE TABLE Orders
(
    order_id     UUID PRIMARY KEY,
    time_slot_id BIGINT       NOT NULL,
    user_id      UUID         NOT NULL,
    order_status VARCHAR(50) NOT NULL
);
