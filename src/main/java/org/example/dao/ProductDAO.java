package org.example.dao;

import org.example.models.Product;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Data Access Object for Product operations
 */
public class ProductDAO {
    private static final Logger logger = Logger.getLogger(ProductDAO.class.getName());

    /**
     * Create a new product
     * @param product Product object to create
     * @return true if successful
     */
    public boolean createProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price, stock, category, image_url, is_active, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setInt(4, product.getStock());
            pstmt.setString(5, product.getCategory());
            pstmt.setString(6, product.getImageUrl());
            pstmt.setBoolean(7, product.isActive());
            pstmt.setTimestamp(8, Timestamp.valueOf(product.getCreatedAt()));
            pstmt.setTimestamp(9, Timestamp.valueOf(product.getUpdatedAt()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error creating product: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get product by ID
     * @param id Product ID
     * @return Product object or null
     */
    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToProduct(rs);
            }
        } catch (SQLException e) {
            logger.severe("Error getting product by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all products
     * @return List of Product objects
     */
    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM products WHERE is_active = true ORDER BY created_at DESC";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error getting all products: " + e.getMessage());
        }
        return products;
    }

    /**
     * Get products by category
     * @param category Product category
     * @return List of Product objects
     */
    public List<Product> getProductsByCategory(String category) {
        String sql = "SELECT * FROM products WHERE category = ? AND is_active = true ORDER BY created_at DESC";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error getting products by category: " + e.getMessage());
        }
        return products;
    }

    /**
     * Search products by name
     * @param searchTerm Search term
     * @return List of Product objects
     */
    public List<Product> searchProducts(String searchTerm) {
        String sql = "SELECT * FROM products WHERE (name LIKE ? OR description LIKE ?) AND is_active = true ORDER BY created_at DESC";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error searching products: " + e.getMessage());
        }
        return products;
    }

    /**
     * Update product
     * @param product Product object to update
     * @return true if successful
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock = ?, " +
                     "category = ?, image_url = ?, is_active = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setInt(4, product.getStock());
            pstmt.setString(5, product.getCategory());
            pstmt.setString(6, product.getImageUrl());
            pstmt.setBoolean(7, product.isActive());
            pstmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(9, product.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error updating product: " + e.getMessage());
        }
        return false;
    }

    /**
     * Update product stock
     * @param productId Product ID
     * @param newStock New stock quantity
     * @return true if successful
     */
    public boolean updateStock(int productId, int newStock) {
        String sql = "UPDATE products SET stock = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, newStock);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, productId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error updating product stock: " + e.getMessage());
        }
        return false;
    }

    /**
     * Delete product (soft delete)
     * @param id Product ID
     * @return true if successful
     */
    public boolean deleteProduct(int id) {
        String sql = "UPDATE products SET is_active = false, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(2, id);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error deleting product: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get all categories
     * @return List of unique categories
     */
    public List<String> getAllCategories() {
        String sql = "SELECT DISTINCT category FROM products WHERE is_active = true ORDER BY category";
        List<String> categories = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            logger.severe("Error getting categories: " + e.getMessage());
        }
        return categories;
    }

    /**
     * Map ResultSet to Product object
     * @param rs ResultSet
     * @return Product object
     * @throws SQLException if mapping fails
     */
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStock(rs.getInt("stock"));
        product.setCategory(rs.getString("category"));
        product.setImageUrl(rs.getString("image_url"));
        product.setActive(rs.getBoolean("is_active"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            product.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            product.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return product;
    }
}
