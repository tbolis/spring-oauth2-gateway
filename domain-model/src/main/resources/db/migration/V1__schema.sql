-- Sequences
CREATE SEQUENCE hibernate_sequence;
CREATE SEQUENCE persistent_audit_event_event_id_seq;
CREATE SEQUENCE user_id_seq;
-- Tables
CREATE TABLE auth_authority (
  name        CHARACTER VARYING(50) NOT NULL,
  description CHARACTER VARYING(100),
  CONSTRAINT pk_authority PRIMARY KEY (name)
);
CREATE TABLE persistent_audit_event (
  event_id   BIGINT DEFAULT nextval('persistent_audit_event_event_id_seq' :: REGCLASS) NOT NULL,
  principal  CHARACTER VARYING(255)                                                    NOT NULL,
  event_date TIMESTAMP(6) WITHOUT TIME ZONE,
  event_type CHARACTER VARYING(255),
  CONSTRAINT pk_persistent_audit_event PRIMARY KEY (event_id)
);
CREATE TABLE persistent_audit_evt_data (
  event_id BIGINT                 NOT NULL,
  name     CHARACTER VARYING(255) NOT NULL,
  value    CHARACTER VARYING(255),
  PRIMARY KEY (event_id, name)
);
CREATE TABLE auth_user (
  id                 BIGINT DEFAULT nextval('user_id_seq' :: REGCLASS) NOT NULL,
  login              CHARACTER VARYING(50)                             NOT NULL,
  password           CHARACTER VARYING(100),
  first_name         CHARACTER VARYING(50),
  last_name          CHARACTER VARYING(50),
  email              CHARACTER VARYING(100),
  activated          BOOLEAN                                           NOT NULL,
  lang_key           CHARACTER VARYING(5),
  activation_key     CHARACTER VARYING(20),
  reset_key          CHARACTER VARYING(20),
  created_by         CHARACTER VARYING(50)                             NOT NULL,
  created_date       TIMESTAMP(6) WITHOUT TIME ZONE                    NOT NULL,
  reset_date         TIMESTAMP(6) WITHOUT TIME ZONE,
  last_modified_by   CHARACTER VARYING(50),
  last_modified_date TIMESTAMP(6) WITHOUT TIME ZONE,
  CONSTRAINT pk_auth_user PRIMARY KEY (id),
  UNIQUE (email),
  UNIQUE (login)
);
CREATE TABLE auth_user_authority (
  user_id        BIGINT                NOT NULL,
  authority_name CHARACTER VARYING(50) NOT NULL,
  PRIMARY KEY (user_id, authority_name)
);
CREATE TABLE oauth_access_token (
  token_id          CHARACTER VARYING(255),
  token             BYTEA,
  authentication_id CHARACTER VARYING(255) NOT NULL,
  user_name         CHARACTER VARYING(255),
  client_id         CHARACTER VARYING(255),
  authentication    BYTEA,
  refresh_token     CHARACTER VARYING(255),
  CONSTRAINT pk_oauth_access_token PRIMARY KEY (authentication_id)
);
CREATE TABLE oauth_approvals (
  userId         CHARACTER VARYING(255),
  clientId       CHARACTER VARYING(255),
  scope          CHARACTER VARYING(255),
  status         CHARACTER VARYING(255),
  expiresAt      TIMESTAMP(6) WITHOUT TIME ZONE,
  lastModifiedAt TIMESTAMP(6) WITHOUT TIME ZONE
);
CREATE TABLE oauth_client_details (
  client_id               CHARACTER VARYING(255) NOT NULL,
  resource_ids            CHARACTER VARYING(255),
  client_secret           CHARACTER VARYING(255),
  scope                   CHARACTER VARYING(255),
  authorized_grant_types  CHARACTER VARYING(255),
  web_server_redirect_uri CHARACTER VARYING(255),
  authorities             CHARACTER VARYING(255),
  access_token_validity   INTEGER,
  refresh_token_validity  INTEGER,
  additional_information  CHARACTER VARYING(4096),
  autoapprove             CHARACTER VARYING(4096),
  CONSTRAINT pk_oauth_client_details PRIMARY KEY (client_id)
);
CREATE TABLE oauth_client_token (
  token_id          CHARACTER VARYING(255),
  token             BYTEA,
  authentication_id CHARACTER VARYING(255),
  user_name         CHARACTER VARYING(255),
  client_id         CHARACTER VARYING(255)
);
CREATE TABLE oauth_code (
  code           CHARACTER VARYING(255),
  authentication BYTEA
);
CREATE TABLE oauth_refresh_token (
  token_id       CHARACTER VARYING(255),
  token          BYTEA,
  authentication BYTEA
);
-- Constraints
-- formatter:off
ALTER TABLE persistent_audit_evt_data ADD CONSTRAINT fk_evt_pers_audit_evt_data FOREIGN KEY (event_id) REFERENCES persistent_audit_event (event_id);
ALTER TABLE auth_user_authority ADD CONSTRAINT fk_authority_name FOREIGN KEY (authority_name) REFERENCES auth_authority (name);
ALTER TABLE auth_user_authority ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES auth_user (id);
ALTER TABLE oauth_access_token ADD CONSTRAINT fk_oauth_access_tokn_user_name FOREIGN KEY (user_name) REFERENCES auth_user (login);
ALTER TABLE oauth_client_token ADD CONSTRAINT fk_oauth_client_tokn_user_name FOREIGN KEY (user_name) REFERENCES auth_user (login);
-- formatter:on