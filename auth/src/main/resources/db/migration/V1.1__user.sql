create table user_tbl
(
    id         UUID                     NOT NULL,
    version    INTEGER,
    created_at TIMESTAMP with time zone NOT NULL,
    updated_at TIMESTAMP with time zone NOT NULL,
    firstname TEXT                     NOT NULL,
    lastname  TEXT                     NOT NULL,
    email      TEXT                     NOT NULL,
    password   TEXT                     NOT NULL,
    PRIMARY KEY (id)
);