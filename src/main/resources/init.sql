--Standard roles

INSERT INTO roles (name, description, predefined)
VALUES ('ROLE_USER', 'Default User Role', TRUE);

INSERT INTO roles (name, description, predefined)
VALUES ('ROLE_MOD', 'Default Moderator Role', TRUE);

INSERT INTO roles (name, description, predefined)
VALUES ('ROLE_ADMIN', 'Default Administrator Role', TRUE);

INSERT INTO roles (name, description, predefined)
VALUES ('ROLE_OWNER', 'Default Owner Role, a.k.a. Super-Admin', TRUE);

INSERT INTO roles (name, description, predefined)
VALUES ('ROLE_GUEST', 'Dummy Role for Guests', TRUE);
