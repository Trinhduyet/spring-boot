CREATE TABLE categories (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    slug VARCHAR(96) NOT NULL,
    active BIT NOT NULL CONSTRAINT df_categories_active DEFAULT 1,
    version BIGINT NOT NULL CONSTRAINT df_categories_version DEFAULT 0,
    created_at DATETIME2(7) NOT NULL,
    updated_at DATETIME2(7) NOT NULL
);
CREATE UNIQUE INDEX ux_categories_name_lower ON categories(name);
CREATE UNIQUE INDEX ux_categories_slug_lower ON categories(slug);

CREATE TABLE product_categories (
    product_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    created_at DATETIME2(7) NOT NULL CONSTRAINT df_product_categories_created DEFAULT SYSUTCDATETIME(),
    CONSTRAINT pk_product_categories PRIMARY KEY (product_id, category_id),
    CONSTRAINT fk_product_categories_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_product_categories_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE NO ACTION
);
CREATE INDEX ix_product_categories_category ON product_categories(category_id, product_id);

CREATE TABLE inventory (
    product_id BIGINT NOT NULL PRIMARY KEY,
    on_hand INT NOT NULL CONSTRAINT df_inventory_on_hand DEFAULT 0,
    reserved INT NOT NULL CONSTRAINT df_inventory_reserved DEFAULT 0,
    version BIGINT NOT NULL CONSTRAINT df_inventory_version DEFAULT 0,
    updated_at DATETIME2(7) NOT NULL CONSTRAINT df_inventory_updated DEFAULT SYSUTCDATETIME(),
    CONSTRAINT ck_inventory_on_hand CHECK (on_hand >= 0),
    CONSTRAINT ck_inventory_reserved CHECK (reserved >= 0 AND reserved <= on_hand),
    CONSTRAINT fk_inventory_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE NO ACTION
);

CREATE TABLE inventory_movements (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    type VARCHAR(24) NOT NULL,
    quantity INT NOT NULL,
    reference VARCHAR(200) NOT NULL,
    created_at DATETIME2(7) NOT NULL CONSTRAINT df_inventory_movements_created DEFAULT SYSUTCDATETIME(),
    CONSTRAINT ck_inventory_movements_type CHECK (type IN ('ADJUST', 'RESERVE', 'RELEASE')),
    CONSTRAINT ck_inventory_movements_quantity CHECK (quantity <> 0),
    CONSTRAINT fk_inventory_movements_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE NO ACTION
);
CREATE INDEX ix_inventory_movements_product_id ON inventory_movements(product_id, id DESC);

CREATE TABLE customer_orders (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    actor_id VARCHAR(200) NOT NULL,
    idempotency_key VARCHAR(128) NOT NULL,
    status VARCHAR(24) NOT NULL,
    created_at DATETIME2(7) NOT NULL,
    updated_at DATETIME2(7) NOT NULL,
    version BIGINT NOT NULL CONSTRAINT df_customer_orders_version DEFAULT 0,
    CONSTRAINT ck_customer_orders_status CHECK (status IN ('RESERVED', 'CANCELLED')),
    CONSTRAINT ux_order_actor_idempotency UNIQUE (actor_id, idempotency_key)
);

CREATE TABLE order_lines (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    sku VARCHAR(64) NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(14,2) NOT NULL,
    CONSTRAINT ck_order_lines_quantity CHECK (quantity > 0),
    CONSTRAINT ck_order_lines_price CHECK (unit_price >= 0),
    CONSTRAINT fk_order_lines_order FOREIGN KEY (order_id) REFERENCES customer_orders(id) ON DELETE NO ACTION,
    CONSTRAINT fk_order_lines_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE NO ACTION
);
CREATE INDEX ix_order_lines_order ON order_lines(order_id, id);

CREATE TABLE audit_records (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    actor_id VARCHAR(200) NOT NULL,
    action VARCHAR(80) NOT NULL,
    resource_type VARCHAR(80) NOT NULL,
    resource_id VARCHAR(120) NOT NULL,
    created_at DATETIME2(7) NOT NULL
);
CREATE INDEX ix_audit_records_created ON audit_records(created_at DESC);
