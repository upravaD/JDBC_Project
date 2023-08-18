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