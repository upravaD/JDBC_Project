package org.project.util;

public enum Queries {

    PERMISSION_CREATE("INSERT INTO permissions (id, permission_name) VALUES (DEFAULT, ?)"),
    PERMISSION_GET_ALL("SELECT * FROM permissions"),
    PERMISSION_FIND_BY_ID("SELECT * FROM permissions WHERE id = ?"),
    PERMISSION_UPDATE("UPDATE permissions SET permission_name = ? WHERE id = ?"),
    PERMISSION_DELETE("DELETE FROM permissions WHERE id = ? AND permission_name = ?"),

    ROLE_CREATE("INSERT INTO roles (id, role_name) VALUES (DEFAULT, ?)"),
    ROLE_GET_ALL("SELECT r.id, r.role_name, p.permission_name FROM roles r LEFT JOIN role_permissions rp ON r.id = rp.role_id LEFT JOIN permissions p ON rp.permission_id = p.id"),
    ROLE_FIND_BY_ID("SELECT r.id, r.role_name, p.permission_name FROM roles r LEFT JOIN role_permissions rp ON r.id = rp.role_id LEFT JOIN permissions p ON rp.permission_id = p.id WHERE r.id = ?"),
    ROLE_UPDATE(""),
    ROLE_DELETE(""),

    USER_CREATE(""),
    USER_GET_ALL(""),
    USER_FIND_BY_ID(""),
    USER_UPDATE(""),
    USER_DELETE("");

    private final String query;

    Queries(String query) {
        this.query = query;
    }

    public String get() {
        return query;
    }
}
