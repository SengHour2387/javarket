package org.example.screens;

import java.sql.SQLException;

import org.example.DatabaseConnector;
import org.example.models.User;

public class AppController {

    private DatabaseConnector connector;
    User currentUser = new User();

    public AppController(DatabaseConnector connector, User user) {
        this.connector = connector;

        currentUser = user;
        try {
            connector.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public DatabaseConnector getConnector() {
        return connector;
    }


}
