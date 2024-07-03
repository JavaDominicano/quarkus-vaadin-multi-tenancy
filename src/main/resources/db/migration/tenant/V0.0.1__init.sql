--   #####   #######  ######   #     #   #####   #######  #     #  ######   #######
--  #     #     #     #     #  #     #  #     #     #     #     #  #     #  #
--  #           #     #     #  #     #  #           #     #     #  #     #  #
--   #####      #     ######   #     #  #           #     #     #  ######   #####
--        #     #     #   #    #     #  #           #     #     #  #   #    #
--  #     #     #     #    #   #     #  #     #     #     #     #  #    #   #
--   #####      #     #     #   #####    #####      #      #####   #     #  #######

CREATE TABLE profile
(
    code     bigserial NOT NULL,
    username varchar(100) NULL,
    "name"   varchar(100) NULL,
    rol      varchar(25) NULL,
    CONSTRAINT profile_pkey PRIMARY KEY (code),
    CONSTRAINT profile_username_key UNIQUE (username)
);
CREATE INDEX idx5em4hwqp4woqsf49dru7fjo80 ON profile USING btree (username);


CREATE TABLE person
(
    code          bigserial    NOT NULL,
    first_name    varchar(50)  NOT NULL,
    last_name     varchar(50)  NOT NULL,
    email         varchar(100) NOT NULL,
    phone         varchar(50)  NOT NULL,
    date_of_birth date         NOT NULL,
    occupation    varchar(100) NOT NULL,
    "role"        varchar(50)  NOT NULL,
    important     bool         NOT NULL,
    CONSTRAINT person_pkey PRIMARY KEY (code)
);
CREATE INDEX idx7d9wr9eid9hei15m3t98w0lya ON person USING btree (last_name);
CREATE INDEX idxexqo3u81yamdnfwndpx8svr0w ON person USING btree (first_name);
CREATE INDEX idxfwmwi44u55bo4rvwsv0cln012 ON person USING btree (email);