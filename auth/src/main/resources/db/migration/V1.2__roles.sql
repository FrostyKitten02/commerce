CREATE TABLE role
(
    id          UUID PRIMARY KEY,
    name        TEXT                     NOT NULL UNIQUE,
    description TEXT,
    version     INTEGER,
    created_at  TIMESTAMP with time zone NOT NULL,
    updated_at  TIMESTAMP with time zone NOT NULL
);

CREATE TABLE group_tbl
(
    id          UUID PRIMARY KEY,
    name        TEXT                     NOT NULL UNIQUE,
    description TEXT,
    version     INTEGER,
    created_at  TIMESTAMP with time zone NOT NULL,
    updated_at  TIMESTAMP with time zone NOT NULL
);

CREATE TABLE group_role
(
    id         UUID PRIMARY KEY,
    group_id   UUID                     NOT NULL,
    role_id    UUID                     NOT NULL,
    version    INTEGER,
    created_at TIMESTAMP with time zone NOT NULL,
    updated_at TIMESTAMP with time zone NOT NULL,
    FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES group_tbl (id) ON DELETE CASCADE
);

CREATE TABLE user_group
(
    id         UUID PRIMARY KEY,
    user_id    UUID                     NOT NULL,
    group_id   UUID                     NOT NULL,
    version    INTEGER,
    created_at TIMESTAMP with time zone NOT NULL,
    updated_at TIMESTAMP with time zone NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_tbl (id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES group_tbl (id) ON DELETE CASCADE
);

-- default roles and groups for customers and admins
-- customer
WITH customer_group AS (
    INSERT INTO group_tbl (id, name, description, version, created_at, updated_at)
    VALUES (gen_random_uuid(), 'CUSTOMER', 'Group for customer users', 0, NOW(), NOW())
    RETURNING id
),
customer_role AS (
    INSERT INTO role (id, name, description, version, created_at, updated_at)
    VALUES (gen_random_uuid(), 'CUSTOMER', 'Role for customer users', 0, NOW(), NOW())
    RETURNING id
)
INSERT INTO group_role (id, group_id, role_id, version, created_at, updated_at)
VALUES (gen_random_uuid(), (SELECT id FROM customer_group), (SELECT id FROM customer_role), 0, NOW(), NOW());

-- admin
WITH admin_group AS (
    INSERT INTO group_tbl (id, name, description, version, created_at, updated_at)
    VALUES (gen_random_uuid(), 'ADMIN', 'Group for admin users', 0, NOW(), NOW())
    RETURNING id
)
,admin_role AS (
    INSERT INTO role (id, name, description, version, created_at, updated_at)
    VALUES (gen_random_uuid(), 'ADMIN', 'Role for admin users', 0, NOW(), NOW())
    RETURNING id
)
INSERT INTO group_role (id, group_id, role_id, version, created_at, updated_at)
VALUES (gen_random_uuid(), (SELECT id FROM admin_group), (SELECT id FROM admin_role), 0, NOW(), NOW());

