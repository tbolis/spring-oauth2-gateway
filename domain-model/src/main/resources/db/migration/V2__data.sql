-- Initial Data

-- @formatter:off

-- Authorities
INSERT INTO auth_authority (NAME, DESCRIPTION) VALUES ('ROLE_ADMIN', 'System Administrator');
INSERT INTO auth_authority (NAME, DESCRIPTION) VALUES ('ROLE_USER', 'Authorized User');

-- Users
INSERT INTO auth_user (id, login, password, first_name, last_name, email, activated, lang_key, created_by, created_date) VALUES (1, 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Administrator', 'Administrator','admin@localhost', TRUE, 'en', 'system', current_timestamp);
INSERT INTO auth_user_authority (user_id, authority_name) VALUES (1, 'ROLE_ADMIN');

-- OAuth2 Clients
INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('backend', 'OAuth2Server', '$2a$10$K98cVwJOlwUOIAYrHVICveUZh.jj9N0D3XSVHiAmevCNsJ9ulwLrq', 'read,write', 'password,refresh_token', null, 'ROLE_ADMIN', 900, 172800, null, null);
INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('client', 'OAuth2Server', 'bSZRuJkLDgCWupmH', 'read,write', 'authorization_code', 'http://localhost:8082/home', 'ROLE_USER', 900, 172800, null, 'true');

-- @formatter:on