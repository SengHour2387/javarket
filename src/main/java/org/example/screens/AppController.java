package org.example.screens;

import org.example.DatabaseConnector;

import java.sql.SQLException;

public class AppController {


    public AppController(DatabaseConnector connector) {
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
