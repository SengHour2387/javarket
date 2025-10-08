package org.example.screens;

import org.example.DatabaseConnector;
import org.example.models.User;
import org.example.models.Prodcut;

import java.sql.SQLException;

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

    public boolean addProduct(Prodcut product) {
        try {
            String sql = "INSERT INTO products_tbl (name, description, price, image, stock, category_id, seller_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            int rowsAffected = connector.runCUD(sql, 
                product.getName(), 
                product.getDescription(), 
                product.getPrice(), 
                product.getImage(),
                product.getStock(), 
                product.getCategory_id(), 
                product.getSeller_id()
            );
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
            return false;
        }
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
