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
    
    /**
     * Add a new product to the database
     */
    public boolean addProduct(Prodcut product) {
        try {
            int rowsAffected = connector.runCUD(
                "INSERT INTO products_tbl (name, description, price, image, stock, category_id, seller_id) VALUES (?, ?, ?, ?, ?, ?, ?)",
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
    
    /**
     * Get all products from the database
     */
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
    
    /**
     * Get a product by ID
     */
    public Prodcut getProductById(int id) {
        try {
            ResultSet resultSet = connector.runSelect("SELECT * FROM products_tbl WHERE id = ?", id);
            if (resultSet.next()) {
                return new Prodcut(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getDouble("price"),
                    resultSet.getString("image"),
                    resultSet.getInt("stock"),
                    resultSet.getInt("category_id"),
                    resultSet.getInt("seller_id")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting product by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Update a product
     */
    public boolean updateProduct(Prodcut product) {
        try {
            int rowsAffected = connector.runCUD(
                "UPDATE products_tbl SET name = ?, description = ?, price = ?, image = ?, stock = ?, category_id = ?, seller_id = ? WHERE id = ?",
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImage(),
                product.getStock(),
                product.getCategory_id(),
                product.getSeller_id(),
                product.getId()
            );
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a product by ID
     */
    public boolean deleteProduct(int id) {
        try {
            int rowsAffected = connector.runCUD("DELETE FROM products_tbl WHERE id = ?", id);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Search products by name
     */
    public List<Prodcut> searchProducts(String searchTerm) {
        List<Prodcut> products = new ArrayList<>();
        try {
            ResultSet resultSet = connector.runSelect(
                "SELECT * FROM products_tbl WHERE name LIKE ? OR description LIKE ? ORDER BY id",
                "%" + searchTerm + "%", "%" + searchTerm + "%"
            );
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
            System.err.println("Error searching products: " + e.getMessage());
        }
        return products;
    }
    
    /**
     * Get product count
     */
    public int getProductCount() {
        try {
            ResultSet resultSet = connector.runSelect("SELECT COUNT(*) as count FROM products_tbl");
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Error getting product count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Close the database connection
     */
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
