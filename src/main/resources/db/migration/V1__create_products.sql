CREATE TABLE products (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    sku VARCHAR(64) NOT NULL,
    name VARCHAR(200) NOT NULL,
    price DECIMAL(14,2) NOT NULL CONSTRAINT ck_products_price CHECK (price >= 0),
    active BIT NOT NULL CONSTRAINT df_products_active DEFAULT 1,
    version BIGINT NOT NULL CONSTRAINT df_products_version DEFAULT 0,
    created_at DATETIME2(7) NOT NULL,
    updated_at DATETIME2(7) NOT NULL
);
CREATE UNIQUE INDEX ux_products_sku_lower ON products(sku);
CREATE INDEX ix_products_name ON products(name);
