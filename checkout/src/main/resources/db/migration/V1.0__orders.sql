CREATE TABLE orders
(
    id                   UUID                     NOT NULL,
    version              INTEGER,
    created_at           TIMESTAMP with time zone NOT NULL,
    updated_at           TIMESTAMP with time zone NOT NULL,
    user_id              UUID                     NOT NULL,
    cart_id              UUID,
    shipping_address     VARCHAR(500),
    shipping_city        VARCHAR(100),
    shipping_postal_code VARCHAR(20),
    shipping_country     VARCHAR(100),
    total_amount         DECIMAL(10, 2)           NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE order_items
(
    id           UUID                     NOT NULL,
    version      INTEGER,
    created_at   TIMESTAMP with time zone NOT NULL,
    updated_at   TIMESTAMP with time zone NOT NULL,
    order_id     UUID                     NOT NULL,
    product_id   UUID                     NOT NULL,
    product_name TEXT,
    product_sku  TEXT,
    unit_price   DECIMAL(10, 2)           NOT NULL,
    quantity     INTEGER                  NOT NULL,
    line_total   DECIMAL(10, 2)           NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);