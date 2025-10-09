package org.example;

import org.example.models.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SimpleOrderManager {

    private final DatabaseConnector connector;

    public SimpleOrderManager() {
        this.connector = new DatabaseConnector();
        try {
            connector.connect();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public boolean addOrder(Order order) {
        try {
            int rows = connector.runCUD(
                "INSERT INTO orders_tbl (product_id, buyer_id, quantity, total_price, status, created_at) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)",
                order.getProductId(),
                order.getBuyerId(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getStatus()
            );
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error adding order: " + e.getMessage());
            return false;
        }
    }

    public boolean updateOrderStatus(int orderId, String status) {
        try {
            int rows = connector.runCUD(
                "UPDATE orders_tbl SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?",
                status, orderId
            );
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating order: " + e.getMessage());
            return false;
        }
    }

    public Order getOrderById(int id) {
        try {
            ResultSet rs = connector.runSelect("SELECT * FROM orders_tbl WHERE id = ?", id);
            if (rs.next()) {
                return new Order(
                    rs.getInt("id"),
                    rs.getInt("product_id"),
                    rs.getInt("buyer_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("total_price"),
                    rs.getString("status"),
                    rs.getString("created_at"),
                    rs.getString("updated_at")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting order: " + e.getMessage());
        }
        return null;
    }

    public List<Order> getOrdersByBuyer(int buyerId) {
        List<Order> orders = new ArrayList<>();
        try {
            ResultSet rs = connector.runSelect("SELECT * FROM orders_tbl WHERE buyer_id = ? ORDER BY id DESC", buyerId);
            while (rs.next()) {
                orders.add(new Order(
                    rs.getInt("id"),
                    rs.getInt("product_id"),
                    rs.getInt("buyer_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("total_price"),
                    rs.getString("status"),
                    rs.getString("created_at"),
                    rs.getString("updated_at")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting orders: " + e.getMessage());
        }
        return orders;
    }
}


