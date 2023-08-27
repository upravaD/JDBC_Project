/*Создание базы данных*/
CREATE DATABASE postgres;
CREATE SCHEMA public;


/*Создание таблиц*/
CREATE TABLE roles (id SERIAL PRIMARY KEY,
                    role_name VARCHAR(50) NOT NULL);


CREATE TABLE permissions (id SERIAL PRIMARY KEY,
                          permission_name VARCHAR(50) NOT NULL);


CREATE TABLE users (id SERIAL PRIMARY KEY,
                    username VARCHAR(50) NOT NULL,
                    role_id INT,
                    FOREIGN KEY (role_id) REFERENCES roles(id));


CREATE TABLE role_permissions (role_id INT,
                               permission_id INT,
                               PRIMARY KEY (role_id, permission_id),
                               FOREIGN KEY (role_id) REFERENCES roles(id),
                               FOREIGN KEY (permission_id) REFERENCES permissions(id));


/*Заполнение таблиц*/
INSERT INTO users (id, username, role_id) VALUES (DEFAULT, 'User_1', 1);
INSERT INTO users (id, username, role_id) VALUES (DEFAULT, 'User_2', 2);
INSERT INTO users (id, username, role_id) VALUES (DEFAULT, 'User_3', 3);
INSERT INTO users (id, username, role_id) VALUES (DEFAULT, 'User_4', 4);
INSERT INTO users (id, username, role_id) VALUES (DEFAULT, 'User_5', 5);


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


INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 1);
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 2);
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 3);
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 4);
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 6);

INSERT INTO role_permissions (role_id, permission_id) VALUES (2, 2);
INSERT INTO role_permissions (role_id, permission_id) VALUES (2, 5);

INSERT INTO role_permissions (role_id, permission_id) VALUES (3, 3);
INSERT INTO role_permissions (role_id, permission_id) VALUES (3, 4);
INSERT INTO role_permissions (role_id, permission_id) VALUES (3, 5);

INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 5);
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 6);

INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 5);
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 7);