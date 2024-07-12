--   #####   #######  ######   #     #   #####   #######  #     #  ######   #######
--  #     #     #     #     #  #     #  #     #     #     #     #  #     #  #
--  #           #     #     #  #     #  #           #     #     #  #     #  #
--   #####      #     ######   #     #  #           #     #     #  ######   #####
--        #     #     #   #    #     #  #           #     #     #  #   #    #
--  #     #     #     #    #   #     #  #     #     #     #     #  #    #   #
--   #####      #     #     #   #####    #####      #      #####   #     #  #######

CREATE TABLE users
(
    username   varchar(100) NOT NULL,
    "password" varchar(100) NULL,
    role       varchar(25)  NOT NULL,
    CONSTRAINT users_username_key UNIQUE (username)
);
CREATE INDEX idxsdse3b343v3353fdfdfdfd34df ON users USING btree (username);


CREATE TABLE tenant
(
    tenant_id varchar(255) NOT NULL,
    "name"    varchar(100) NOT NULL,
    slogan    varchar(100) NULL,
    "type"    varchar(100) NULL,
    phone     varchar(100) NULL,
    email     varchar(100) NULL,
    website   varchar(100) NULL,
    address   varchar(100) NULL,
    logo      varchar(100) NULL,
    CONSTRAINT tenant_pkey PRIMARY KEY (tenant_id)
);
CREATE INDEX idxdcxf3ksi0gyn1tieeq0id96lm ON tenant USING btree (name);


CREATE TABLE tenant_user
(
    id        bigserial    NOT NULL,
    tenant_id varchar(100) NOT NULL,
    username  varchar(100) NOT NULL,
    disabled  bool         NOT NULL,
    CONSTRAINT tenant_user_pkey PRIMARY KEY (id)
);
ALTER TABLE tenant_user
    ADD CONSTRAINT fk1amqswdsbt9pk1nwd1sejuobn FOREIGN KEY (username) REFERENCES users (username);
ALTER TABLE tenant_user
    ADD CONSTRAINT fkjbyohwto7pt48xywupgf4vjc7 FOREIGN KEY (tenant_id) REFERENCES tenant (tenant_id);

CREATE UNIQUE INDEX tenant_user_tenant_id_idx ON tenant_user (tenant_id, username);
