package org.example.screens;

import org.example.DatabaseConnector;
import org.example.models.User;

import java.sql.SQLException;

public class AppController {

    User currentUser = new User();

    public AppController(DatabaseConnector connector, User user) {

        currentUser = user;
        try {
            connector.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addProduct() {

        return false;
    }
    public boolean updateProduct() {

        return false;
    }

    public boolean addOrder() {

        return false;
    }

    public boolean updateOrder() {
        return false;
    }


}
