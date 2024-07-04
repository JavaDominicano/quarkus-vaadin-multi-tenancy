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
    tenant_id varchar(100) NOT NULL,
    "name"   varchar(100) NULL,
    CONSTRAINT tenant_pkey PRIMARY KEY (tenant_id)
);
CREATE INDEX idxdcxf3ksi0gyn1tieeq0id96lm ON tenant USING btree (name);


CREATE TABLE tenant_user
(
    tenant_id varchar(100) NOT NULL,
    username  varchar(100) NOT NULL,
    disabled  bool         NOT NULL,
    CONSTRAINT tenant_user_pkey PRIMARY KEY (tenant_id, username)
);
ALTER TABLE tenant_user
    ADD CONSTRAINT fk1amqswdsbt9pk1nwd1sejuobn FOREIGN KEY (username) REFERENCES users (username);
ALTER TABLE tenant_user
    ADD CONSTRAINT fkjbyohwto7pt48xywupgf4vjc7 FOREIGN KEY (tenant_id) REFERENCES tenant (tenant_id);