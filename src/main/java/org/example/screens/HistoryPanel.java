package org.example.screens;

import org.example.DatabaseConnector;
import org.example.models.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoryPanel extends JPanel {
    
    private JPanel historyItemsPanel;
    private JScrollPane scrollPane;
    private JLabel totalOrdersLabel;
    private JButton refreshButton;
    private JComboBox<String> statusFilter;
    private DatabaseConnector connector;
    private List<Order> orders;
    
    public HistoryPanel() {
        setLayout(new BorderLayout());
        connector = new DatabaseConnector();
        orders = new ArrayList<>();
        try {
            connector.connect();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        initComponents();
        loadOrderHistory();
    }
    
    private void initComponents() {
        // Header panel with title and controls
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Order History");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        
        // Filter and refresh controls
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JLabel filterLabel = new JLabel("Filter:");
        statusFilter = new JComboBox<>(new String[]{"All", "Pending", "Completed", "Cancelled"});
        statusFilter.setPreferredSize(new Dimension(120, 25));
        
        refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(20, 120, 60));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(refreshButton.getFont().deriveFont(Font.BOLD, 12f));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadOrderHistory();
            }
        });
        
        controlsPanel.add(filterLabel);
        controlsPanel.add(statusFilter);
        controlsPanel.add(Box.createHorizontalStrut(10));
        controlsPanel.add(refreshButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(controlsPanel, BorderLayout.EAST);
        
        // History items panel
        historyItemsPanel = new JPanel();
        historyItemsPanel.setLayout(new BoxLayout(historyItemsPanel, BoxLayout.Y_AXIS));
        historyItemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        scrollPane = new JScrollPane(historyItemsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Bottom panel with statistics
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        totalOrdersLabel = new JLabel("Total Orders: 0");
        totalOrdersLabel.setFont(totalOrdersLabel.getFont().deriveFont(Font.BOLD, 14f));
        totalOrdersLabel.setForeground(new Color(20, 120, 60));
        
        bottomPanel.add(totalOrdersLabel);
        
        // Add components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void loadOrderHistory() {
        orders.clear();
        historyItemsPanel.removeAll();
        
        try {
            String sql = "SELECT o.*, p.name as product_name FROM orders_tbl o " +
                        "LEFT JOIN products_tbl p ON o.product_id = p.id " +
                        "ORDER BY o.created_at DESC";
            
            ResultSet resultSet = connector.runSelect(sql);
            
            while (resultSet.next()) {
                Order order = new Order(
                    resultSet.getInt("id"),
                    resultSet.getInt("product_id"),
                    resultSet.getInt("buyer_id"),
                    resultSet.getInt("quantity"),
                    resultSet.getDouble("total_price"),
                    resultSet.getString("status"),
                    resultSet.getString("created_at"),
                    resultSet.getString("updated_at")
                );
                orders.add(order);
                
                String productName = resultSet.getString("product_name");
                if (productName == null) productName = "Unknown Product";
                
                addHistoryItem(
                    String.valueOf(order.getId()),
                    productName,
                    String.format("$%.2f", order.getTotalPrice()),
                    order.getStatus(),
                    order.getCreatedAt()
                );
            }
            
            if (orders.isEmpty()) {
                JLabel emptyLabel = new JLabel("No order history found");
                emptyLabel.setFont(emptyLabel.getFont().deriveFont(Font.ITALIC, 14f));
                emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
                emptyLabel.setForeground(Color.GRAY);
                historyItemsPanel.add(emptyLabel);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading order history: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            
            // Show empty state on error
            JLabel errorLabel = new JLabel("Error loading order history");
            errorLabel.setFont(errorLabel.getFont().deriveFont(Font.ITALIC, 14f));
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            errorLabel.setForeground(Color.RED);
            historyItemsPanel.add(errorLabel);
        }
        
        historyItemsPanel.revalidate();
        historyItemsPanel.repaint();
        updateTotalOrders();
    }
    
    public void addHistoryItem(String orderId, String productName, String total, String status, String date) {
        JPanel itemPanel = createHistoryItemPanel(orderId, productName, total, status, date);
        historyItemsPanel.add(itemPanel);
        historyItemsPanel.add(Box.createVerticalStrut(8));
        
        historyItemsPanel.revalidate();
        historyItemsPanel.repaint();
    }
    
    private JPanel createHistoryItemPanel(String orderId, String productName, String total, String status, String date) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        // Left side - Order info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        JLabel orderIdLabel = new JLabel("Order #" + orderId);
        orderIdLabel.setFont(orderIdLabel.getFont().deriveFont(Font.BOLD, 14f));
        
        JLabel productLabel = new JLabel(productName);
        productLabel.setFont(productLabel.getFont().deriveFont(12f));
        productLabel.setForeground(new Color(60, 60, 60));
        
        JLabel dateLabel = new JLabel("Ordered: " + date);
        dateLabel.setFont(dateLabel.getFont().deriveFont(10f));
        dateLabel.setForeground(Color.GRAY);
        
        infoPanel.add(orderIdLabel);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(productLabel);
        infoPanel.add(Box.createVerticalStrut(2));
        infoPanel.add(dateLabel);
        
        // Right side - Status and total
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel totalLabel = new JLabel(total);
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 16f));
        totalLabel.setForeground(new Color(20, 120, 60));
        totalLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD, 12f));
        statusLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        // Set status color based on status
        switch (status.toLowerCase()) {
            case "completed":
                statusLabel.setForeground(new Color(20, 120, 60));
                break;
            case "pending":
                statusLabel.setForeground(new Color(255, 193, 7));
                break;
            case "shipped":
                statusLabel.setForeground(new Color(0, 123, 255));
                break;
            case "cancelled":
                statusLabel.setForeground(new Color(220, 53, 69));
                break;
            default:
                statusLabel.setForeground(Color.GRAY);
        }
        
        JButton viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setFont(viewDetailsButton.getFont().deriveFont(10f));
        viewDetailsButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        viewDetailsButton.setPreferredSize(new Dimension(100, 25));
        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOrderDetails(orderId, productName, total, status, date);
            }
        });
        
        statusPanel.add(totalLabel);
        statusPanel.add(Box.createVerticalStrut(4));
        statusPanel.add(statusLabel);
        statusPanel.add(Box.createVerticalStrut(8));
        statusPanel.add(viewDetailsButton);
        
        itemPanel.add(infoPanel, BorderLayout.WEST);
        itemPanel.add(statusPanel, BorderLayout.EAST);
        
        return itemPanel;
    }
    
    private void updateTotalOrders() {
        int totalOrders = orders.size();
        totalOrdersLabel.setText("Total Orders: " + totalOrders);
    }
    
    public void refreshHistory() {
        loadOrderHistory();
    }
    
    private void showOrderDetails(String orderId, String productName, String total, String status, String date) {
        String details = String.format(
            "Order Details:\n\n" +
            "Order ID: %s\n" +
            "Product: %s\n" +
            "Total: %s\n" +
            "Status: %s\n" +
            "Order Date: %s",
            orderId, productName, total, status, date
        );
        
        JOptionPane.showMessageDialog(
            this,
            details,
            "Order Details - #" + orderId,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    public void clearHistory() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to clear all order history? This action cannot be undone.",
            "Clear History",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                // Delete all orders from database
                connector.runCUD("DELETE FROM orders_tbl");
                loadOrderHistory();
                JOptionPane.showMessageDialog(this, "Order history cleared successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error clearing history: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
