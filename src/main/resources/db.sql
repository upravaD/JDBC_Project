CREATE TABLE users (id SERIAL PRIMARY KEY,
                    username VARCHAR(50) NOT NULL);

CREATE TABLE roles (id SERIAL PRIMARY KEY,
                    role_name VARCHAR(50) NOT NULL);

CREATE TABLE permissions (id SERIAL PRIMARY KEY,
                          permission_name VARCHAR(50) NOT NULL);

CREATE TABLE user_roles (user_id INT,
                         role_id INT,
                         PRIMARY KEY (user_id, role_id),
                         FOREIGN KEY (user_id) REFERENCES users(id),
                         FOREIGN KEY (role_id) REFERENCES roles(id));

CREATE TABLE role_permissions (role_id INT,
                               permission_id INT,
                               PRIMARY KEY (role_id, permission_id),
                               FOREIGN KEY (role_id) REFERENCES roles(id),
                               FOREIGN KEY (permission_id) REFERENCES permissions(id)
);



INSERT INTO users (id, username) VALUES (DEFAULT, 'User_1');
INSERT INTO users (id, username) VALUES (DEFAULT, 'User_2');
INSERT INTO users (id, username) VALUES (DEFAULT, 'User_3');
INSERT INTO users (id, username) VALUES (DEFAULT, 'User_4');
INSERT INTO users (id, username) VALUES (DEFAULT, 'User_5');

INSERT INTO roles (id, role_name) VALUES (DEFAULT, 'Директор');
INSERT INTO roles (id, role_name) VALUES (DEFAULT, 'Бухгалтер');
INSERT INTO roles (id, role_name) VALUES (DEFAULT, 'Продавец');
INSERT INTO roles (id, role_name) VALUES (DEFAULT, 'Охранник');
INSERT INTO roles (id, role_name) VALUES (DEFAULT, 'Уборщик');

INSERT INTO permissions (id, permission_name) VALUES (DEFAULT, 'Управление');
INSERT INTO permissions (id, permission_name) VALUES (DEFAULT, 'Финансы');
INSERT INTO permissions (id, permission_name) VALUES (DEFAULT, 'Закупки');
INSERT INTO permissions (id, permission_name) VALUES (DEFAULT, 'Продажа');
INSERT INTO permissions (id, permission_name) VALUES (DEFAULT, 'Отчетность');
INSERT INTO permissions (id, permission_name) VALUES (DEFAULT, 'Безопасность');
INSERT INTO permissions (id, permission_name) VALUES (DEFAULT, 'Чистота');
