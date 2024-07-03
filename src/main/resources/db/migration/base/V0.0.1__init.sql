-- CREATE TABLE common_user
-- (
--     "name"     varchar(255) NULL,
--     "password" varchar(255) NULL,
--     username   varchar(255) NOT NULL,
--     CONSTRAINT common_user_pkey PRIMARY KEY (username)
-- );


-- CREATE TABLE tenant
-- (
--     id int8 NOT NULL,
--     CONSTRAINT tenant_pkey PRIMARY KEY (id)
-- );
--
--
CREATE TABLE users
(
    id         bigserial NOT NULL,
    username   varchar(255) NULL,
    "password" varchar(255) NULL,
    "role"     varchar(255) NULL,
    CONSTRAINT uk_n67vkjwdu5sqqnyg6m79gmvo8 UNIQUE (username),
    CONSTRAINT users_pkey PRIMARY KEY (id)
);
CREATE INDEX idxn67vkjwdu5sqqnyg6m79gmvo8 ON users USING btree (username);