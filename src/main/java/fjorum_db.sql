CREATE TABLE category
(
  id         SERIAL PRIMARY KEY NOT NULL,
  cat_name   VARCHAR(256)       NOT NULL,
  sort_order INT
);
CREATE TABLE category_id_seq
(
  sequence_name VARCHAR NOT NULL,
  last_value    BIGINT  NOT NULL,
  start_value   BIGINT  NOT NULL,
  increment_by  BIGINT  NOT NULL,
  max_value     BIGINT  NOT NULL,
  min_value     BIGINT  NOT NULL,
  cache_value   BIGINT  NOT NULL,
  log_cnt       BIGINT  NOT NULL,
  is_cycled     BOOL    NOT NULL,
  is_called     BOOL    NOT NULL
);
CREATE TABLE reply
(
  id            SERIAL PRIMARY KEY NOT NULL,
  topic_id      BIGINT             NOT NULL,
  user_id       BIGINT             NOT NULL,
  created       TIMESTAMP          NOT NULL,
  modified      TIMESTAMP          NOT NULL,
  reply_content VARCHAR            NOT NULL
);
CREATE TABLE reply_id_seq
(
  sequence_name VARCHAR NOT NULL,
  last_value    BIGINT  NOT NULL,
  start_value   BIGINT  NOT NULL,
  increment_by  BIGINT  NOT NULL,
  max_value     BIGINT  NOT NULL,
  min_value     BIGINT  NOT NULL,
  cache_value   BIGINT  NOT NULL,
  log_cnt       BIGINT  NOT NULL,
  is_cycled     BOOL    NOT NULL,
  is_called     BOOL    NOT NULL
);
CREATE TABLE topic
(
  id          SERIAL PRIMARY KEY NOT NULL,
  category_id BIGINT             NOT NULL,
  topic_name  VARCHAR(256)       NOT NULL,
  created     TIMESTAMP          NOT NULL,
  user_id     BIGINT             NOT NULL,
  open        BOOL               NOT NULL,
  sticky      BOOL               NOT NULL
);
CREATE TABLE topic_id_seq
(
  sequence_name VARCHAR NOT NULL,
  last_value    BIGINT  NOT NULL,
  start_value   BIGINT  NOT NULL,
  increment_by  BIGINT  NOT NULL,
  max_value     BIGINT  NOT NULL,
  min_value     BIGINT  NOT NULL,
  cache_value   BIGINT  NOT NULL,
  log_cnt       BIGINT  NOT NULL,
  is_cycled     BOOL    NOT NULL,
  is_called     BOOL    NOT NULL
);
CREATE TABLE users
(
  id               SERIAL PRIMARY KEY NOT NULL,
  user_name        VARCHAR(256)       NOT NULL,
  pwd_hash         VARCHAR(256),
  confirm_code     VARCHAR(256),
  recover_pwd_code VARCHAR(256),
  email            VARCHAR(256),
  active           BOOL DEFAULT TRUE  NOT NULL,
  moderator        BOOL DEFAULT FALSE NOT NULL,
  administrator    BOOL DEFAULT FALSE NOT NULL
);
CREATE TABLE users_id_seq
(
  sequence_name VARCHAR NOT NULL,
  last_value    BIGINT  NOT NULL,
  start_value   BIGINT  NOT NULL,
  increment_by  BIGINT  NOT NULL,
  max_value     BIGINT  NOT NULL,
  min_value     BIGINT  NOT NULL,
  cache_value   BIGINT  NOT NULL,
  log_cnt       BIGINT  NOT NULL,
  is_cycled     BOOL    NOT NULL,
  is_called     BOOL    NOT NULL
);
ALTER TABLE reply ADD FOREIGN KEY (topic_id) REFERENCES topic (id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE reply ADD FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE topic ADD FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE topic ADD FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE;
CREATE UNIQUE INDEX users_user_name_key ON users (user_name);

--an initial admin user (password: admin)
INSERT INTO users VALUES
  (0, 'admin', '$2a$10$fXBiVP42zuQM1p4.1gZ0L.eWpHbtEUIr3BKE3Ssa5xircp/IJzfVS', '', '', 'admin@fjorum.com', TRUE, TRUE,
   TRUE);