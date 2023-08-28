package org.project.util;

public enum Queries {
    PERMISSION_CREATE("INSERT INTO permissions (id, permission_name) VALUES (DEFAULT, ?)"),
    PERMISSION_GET_ID("SELECT id FROM permissions WHERE permission_name = ?"),
    PERMISSION_GET_ALL("SELECT * FROM permissions"),
    PERMISSION_FIND_BY_ID("SELECT * FROM permissions WHERE id = ?"),
    PERMISSION_UPDATE("UPDATE permissions SET permission_name = ? WHERE id = ?"),
    PERMISSION_DELETE("DELETE FROM permissions WHERE id = ? AND permission_name = ?"),
    PERMISSION_DELETE_PERMISSION_ROLE("DELETE FROM role_permissions WHERE permission_id = ?"),

    ROLE_CREATE("INSERT INTO roles (id, role_name) VALUES (DEFAULT, ?)"),
    ROLE_GET_ID("SELECT id FROM roles WHERE role_name = ?"),
    ROLE_SET_PERMISSIONS("INSERT INTO role_permissions (role_id, permission_id) VALUES (?, ?)"),
    ROLE_GET_ALL("SELECT id FROM roles"),
    ROLE_FIND_BY_ID("SELECT r.id AS role_id, r.role_name, p.id AS permission_id, p.permission_name FROM roles r LEFT JOIN role_permissions rp ON r.id = rp.role_id LEFT JOIN permissions p ON rp.permission_id = p.id WHERE r.id = ?"),
    ROLE_UPDATE("UPDATE roles SET role_name = ? WHERE id = ?"),
    ROLE_DELETE("DELETE FROM roles WHERE id = ? AND role_name = ?"),
    ROLE_DELETE_ROLE_PERMISSION("DELETE FROM role_permissions WHERE role_id = ?"),

    USER_CREATE("INSERT INTO users (id, username, role_id) VALUES (DEFAULT, ?, ?)"),
    USER_GET_ID("SELECT id FROM users WHERE username = ?"),
    USER_GET_ALL("SELECT * FROM users"),
    USER_FIND_BY_ID("SELECT id, username, role_id FROM users WHERE id = ?"),
    USER_UPDATE("UPDATE users SET username = ?, role_id = ? WHERE id = ?"),
    USER_DELETE("DELETE FROM users WHERE id = ? AND username = ?");

    private final String query;

    Queries(String query) {
        this.query = query;
    }

    public String get() {
        return query;
    }
}
