create table product
(
    id          UUID                     NOT NULL,
    version     INTEGER,
    created_at  TIMESTAMP with time zone NOT NULL,
    updated_at  TIMESTAMP with time zone NOT NULL,
    sku         TEXT UNIQUE,
    name        TEXT                     NOT NULL,
    description TEXT,
    price       DECIMAL(8, 2)            NOT NULL,
    PRIMARY KEY (id)
);