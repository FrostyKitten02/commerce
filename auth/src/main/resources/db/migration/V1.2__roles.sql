CREATE TABLE permission
(
    id          UUID PRIMARY KEY,
    name        TEXT                     NOT NULL UNIQUE,
    description TEXT,
    version     INTEGER,
    created_at  TIMESTAMP with time zone NOT NULL,
    updated_at  TIMESTAMP with time zone NOT NULL
);

CREATE TABLE role
(
    id          UUID PRIMARY KEY,
    name        TEXT                     NOT NULL UNIQUE,
    description TEXT,
    version     INTEGER,
    created_at  TIMESTAMP with time zone NOT NULL,
    updated_at  TIMESTAMP with time zone NOT NULL
);

CREATE TABLE role_permission
(
    id            UUID PRIMARY KEY,
    role_id       UUID                     NOT NULL,
    permission_id UUID                     NOT NULL,
    version       INTEGER,
    created_at    TIMESTAMP with time zone NOT NULL,
    updated_at    TIMESTAMP with time zone NOT NULL,
    FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permission (id) ON DELETE CASCADE
);

CREATE TABLE user_role
(
    id         UUID PRIMARY KEY,
    user_id    UUID                     NOT NULL,
    role_id    UUID                     NOT NULL,
    version    INTEGER,
    created_at TIMESTAMP with time zone NOT NULL,
    updated_at TIMESTAMP with time zone NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_tbl (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE
);

-- default roles and groups for customers and admins
-- customer
WITH customer_role AS (
    INSERT INTO role (id, name, description, version, created_at, updated_at)
        VALUES (gen_random_uuid(), 'CUSTOMER', 'Role for customer users', 0, NOW(), NOW())
        RETURNING id),
     customer_permission AS (
         INSERT INTO permission (id, name, description, version, created_at, updated_at)
             VALUES (gen_random_uuid(), 'CUSTOMER', 'Permission for customer users', 0, NOW(), NOW())
             RETURNING id)
INSERT
INTO role_permission (id, role_id, permission_id, version, created_at, updated_at)
VALUES (gen_random_uuid(), (SELECT id FROM customer_role), (SELECT id FROM customer_permission), 0, NOW(), NOW());

-- admin
WITH admin_role AS (
    INSERT INTO role (id, name, description, version, created_at, updated_at)
        VALUES (gen_random_uuid(), 'ADMIN', 'Role for admin users', 0, NOW(), NOW())
        RETURNING id)
   , admin_permission AS (
    INSERT INTO permission (id, name, description, version, created_at, updated_at)
        VALUES (gen_random_uuid(), 'ADMIN', 'Permission for admin users', 0, NOW(), NOW())
        RETURNING id)
INSERT
INTO role_permission (id, role_id, permission_id, version, created_at, updated_at)
VALUES (gen_random_uuid(), (SELECT id FROM admin_role), (SELECT id FROM admin_permission), 0, NOW(), NOW());

