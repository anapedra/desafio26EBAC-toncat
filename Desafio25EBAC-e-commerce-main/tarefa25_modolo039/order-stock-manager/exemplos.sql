CREATE TABLE tb_user (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    moment_registration TIMESTAMP WITHOUT TIME ZONE,
    moment_update TIMESTAMP WITHOUT TIME ZONE,
    main_phone VARCHAR(20),
    cpf VARCHAR(14) UNIQUE,
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(20)
);

CREATE TABLE tb_product (
    id BIGINT PRIMARY KEY,
    moment_registration TIMESTAMP WITHOUT TIME ZONE,
    moment_update TIMESTAMP WITHOUT TIME ZONE,
    short_description VARCHAR(300),
    full_description TEXT,
    product_cost NUMERIC(10,2),
    initial_price NUMERIC(10,2),
    img_url VARCHAR(255)
);

CREATE TABLE tb_order (
    id BIGINT PRIMARY KEY,
    date_order DATE,
    moment TIMESTAMP WITHOUT TIME ZONE,
    status INTEGER,
    client_id BIGINT,
    CONSTRAINT fk_order_client FOREIGN KEY (client_id) REFERENCES tb_user(id)
);

CREATE TABLE tb_order_item (
    order_id BIGINT,
    product_id BIGINT,
    quantity INTEGER,
    price NUMERIC(10,2),
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES tb_order(id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES tb_product(id)
);

CREATE TABLE tb_stock (
    id BIGINT PRIMARY KEY,
    stock_status INTEGER,
    quantidade INTEGER
);

CREATE TABLE tb_stock_restocking (
    id BIGINT PRIMARY KEY,
    quantity INTEGER,
    moment TIMESTAMP WITHOUT TIME ZONE,
    product_id BIGINT NOT NULL,
    CONSTRAINT fk_stock_product FOREIGN KEY (product_id) REFERENCES tb_product(id)
);

-- Criação das sequences para geração de IDs automáticos (se você quiser usar)
CREATE SEQUENCE seq_user START 1 INCREMENT 1 OWNED BY tb_user.id;
CREATE SEQUENCE seq_product START 1 INCREMENT 1 OWNED BY tb_product.id;
CREATE SEQUENCE seq_order START 1 INCREMENT 1 OWNED BY tb_order.id;
CREATE SEQUENCE seq_stock START 1 INCREMENT 1 OWNED BY tb_stock.id;
CREATE SEQUENCE seq_stock_restocking START 1 INCREMENT 1 OWNED BY tb_stock_restocking.id;
