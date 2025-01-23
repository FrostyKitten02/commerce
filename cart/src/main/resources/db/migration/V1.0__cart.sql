CREATE TABLE cart
(
    id         UUID                     NOT NULL,
    version    INTEGER,
    created_at TIMESTAMP with time zone NOT NULL,
    updated_at TIMESTAMP with time zone NOT NULL,
    user_id    UUID                     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE cart_product
(
    id         UUID                     NOT NULL,
    version    INTEGER,
    created_at TIMESTAMP with time zone NOT NULL,
    updated_at TIMESTAMP with time zone NOT NULL,
    cart_id    UUID                     NOT NULL,
    product_id UUID                     NOT NULL,
    quantity   DECIMAL(12, 4)           NOT NULL,
    PRIMARY KEY (id)
)