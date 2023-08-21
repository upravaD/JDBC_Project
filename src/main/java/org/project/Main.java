package org.project;

import org.project.dao.UserDAO;

public class Main {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        System.out.println(userDAO.getALL());
    }
}