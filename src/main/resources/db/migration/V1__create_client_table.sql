CREATE TABLE Client
(
    id        UUID PRIMARY KEY,
    first_name VARCHAR(255) ,
    last_name  VARCHAR(255) ,
    email     VARCHAR(255) NOT NULL
);