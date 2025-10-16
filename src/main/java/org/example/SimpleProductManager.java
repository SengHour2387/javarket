package org.example;

import org.example.models.Prodcut;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SimpleProductManager {
    
    private DatabaseConnector connector;
    
    public SimpleProductManager() {
        this.connector = new DatabaseConnector();
        try {
            connector.connect();
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to database: " + e.getMessage(), e);
        }
    }

    public List<Prodcut> getAllProducts() {
        List<Prodcut> products = new ArrayList<>();
        try {
            ResultSet resultSet = connector.runSelect("SELECT * FROM products_tbl ORDER BY id");
            while (resultSet.next()) {
                Prodcut product = new Prodcut(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getDouble("price"),
                    resultSet.getString("image"),
                    resultSet.getInt("stock"),
                    resultSet.getInt("category_id"),
                    resultSet.getInt("seller_id")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error getting products: " + e.getMessage());
        }
        return products;
    }

    public void close() {
        try {
            if (connector.connection != null && !connector.connection.isClosed()) {
                connector.connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
