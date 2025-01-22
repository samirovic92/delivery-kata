CREATE TABLE TimeSlot
(
    id        BIGINT PRIMARY KEY,
    delivery_mode VARCHAR(20),
    start_time TIMESTAMP,
    end_time   TIMESTAMP,
    reserved  BOOLEAN
);