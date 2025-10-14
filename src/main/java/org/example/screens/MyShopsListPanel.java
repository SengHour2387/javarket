package org.example.screens;

import org.example.ShopManager;
import org.example.models.Shop;
import org.example.models.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MyShopsListPanel extends JPanel {
    private User currentUser;
    private ShopManager shopManager;
    private JPanel shopsListPanel;
    private Runnable onShopSelected;
    private Shop selectedShop;
    
    public MyShopsListPanel(User currentUser, Runnable onShopCreated, java.util.function.Consumer<Shop> onShopSelected) {
        this.currentUser = currentUser;
        this.shopManager = new ShopManager();
        
        setLayout(new BorderLayout());
        initComponents(onShopCreated, onShopSelected);
    }
    
    private void initComponents(Runnable onShopCreated, java.util.function.Consumer<Shop> onShopSelected) {
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        headerPanel.setBackground(new Color(248, 249, 250));
        
        JLabel titleLabel = new JLabel("🏪 My Shops");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 28f));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton createNewShopBtn = new JButton("➕ Create New Shop");
        createNewShopBtn.setFont(createNewShopBtn.getFont().deriveFont(Font.BOLD, 14f));
        createNewShopBtn.setBackground(new Color(40, 167, 69));
        createNewShopBtn.setForeground(Color.WHITE);
        createNewShopBtn.setFocusPainted(false);
        createNewShopBtn.setBorderPainted(false);
        createNewShopBtn.setOpaque(true);
        createNewShopBtn.setPreferredSize(new Dimension(180, 40));
        createNewShopBtn.addActionListener(e -> {
            if (onShopCreated != null) {
                onShopCreated.run();
            }
        });
        
        JButton clearAllBtn = new JButton("🗑️ Clear All");
        clearAllBtn.setFont(clearAllBtn.getFont().deriveFont(Font.BOLD, 14f));
        clearAllBtn.setBackground(new Color(220, 53, 69));
        clearAllBtn.setForeground(Color.WHITE);
        clearAllBtn.setFocusPainted(false);
        clearAllBtn.setBorderPainted(false);
        clearAllBtn.setOpaque(true);
        clearAllBtn.setPreferredSize(new Dimension(140, 40));
        clearAllBtn.addActionListener(e -> handleClearAllShops(onShopSelected));
        
        buttonPanel.add(createNewShopBtn);
        buttonPanel.add(clearAllBtn);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Shops List Panel
        shopsListPanel = new JPanel();
        shopsListPanel.setLayout(new BoxLayout(shopsListPanel, BoxLayout.Y_AXIS));
        shopsListPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        loadShops(onShopSelected);
        
        JScrollPane scrollPane = new JScrollPane(shopsListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadShops(java.util.function.Consumer<Shop> onShopSelected) {
        shopsListPanel.removeAll();
        
        List<Shop> shops = shopManager.getAllShopsByOwnerId(currentUser.getId());
        
        if (shops.isEmpty()) {
            JLabel emptyLabel = new JLabel("No shops yet. Click 'Create New Shop' to start!");
            emptyLabel.setFont(emptyLabel.getFont().deriveFont(Font.ITALIC, 16f));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            shopsListPanel.add(emptyLabel);
        } else {
            JLabel countLabel = new JLabel("You have " + shops.size() + " shop(s)");
            countLabel.setFont(countLabel.getFont().deriveFont(Font.BOLD, 16f));
            countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            shopsListPanel.add(countLabel);
            shopsListPanel.add(Box.createVerticalStrut(20));
            
            for (Shop shop : shops) {
                shopsListPanel.add(createShopCard(shop, onShopSelected));
                shopsListPanel.add(Box.createVerticalStrut(15));
            }
        }
        
        shopsListPanel.revalidate();
        shopsListPanel.repaint();
    }
    
    private JPanel createShopCard(Shop shop, java.util.function.Consumer<Shop> onShopSelected) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Shop Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel("🏪 " + shop.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 20f));
        
        JLabel typeLabel = new JLabel("Type: " + (shop.getType() != null ? shop.getType() : "N/A"));
        typeLabel.setFont(typeLabel.getFont().deriveFont(Font.PLAIN, 14f));
        typeLabel.setForeground(Color.GRAY);
        
        JLabel statusLabel = new JLabel("Status: " + shop.getStatus());
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.PLAIN, 12f));
        statusLabel.setForeground(shop.getStatus().equals("active") ? new Color(40, 167, 69) : Color.ORANGE);
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(typeLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(statusLabel);
        
        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);
        
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setFont(deleteBtn.getFont().deriveFont(Font.BOLD, 12f));
        deleteBtn.setBackground(new Color(220, 53, 69));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setOpaque(true);
        deleteBtn.setPreferredSize(new Dimension(90, 35));
        deleteBtn.addActionListener(e -> handleDeleteShop(shop, onShopSelected));
        
        JButton manageBtn = new JButton("Manage →");
        manageBtn.setFont(manageBtn.getFont().deriveFont(Font.BOLD, 14f));
        manageBtn.setBackground(new Color(0, 123, 255));
        manageBtn.setForeground(Color.WHITE);
        manageBtn.setFocusPainted(false);
        manageBtn.setBorderPainted(false);
        manageBtn.setOpaque(true);
        manageBtn.setPreferredSize(new Dimension(120, 40));
        manageBtn.addActionListener(e -> {
            if (onShopSelected != null) {
                onShopSelected.accept(shop);
            }
        });
        
        buttonsPanel.add(deleteBtn);
        buttonsPanel.add(manageBtn);
        
        cardPanel.add(infoPanel, BorderLayout.CENTER);
        cardPanel.add(buttonsPanel, BorderLayout.EAST);
        
        return cardPanel;
    }
    
    public void refreshShops(java.util.function.Consumer<Shop> onShopSelected) {
        loadShops(onShopSelected);
    }
    
    private void handleDeleteShop(Shop shop, java.util.function.Consumer<Shop> onShopSelected) {
        // Show warning dialog
        int result = JOptionPane.showConfirmDialog(
            this,
            "⚠️ Are you sure you want to delete '" + shop.getName() + "'?\n\n" +
            "This will permanently delete:\n" +
            "• The shop\n" +
            "• All products in this shop\n" +
            "• All associated data\n\n" +
            "This action cannot be undone!",
            "Delete Shop - Warning",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            // Confirm again with a more serious warning
            int confirmResult = JOptionPane.showConfirmDialog(
                this,
                "🚨 FINAL WARNING 🚨\n\n" +
                "You are about to permanently delete '" + shop.getName() + "'.\n" +
                "This CANNOT be undone!\n\n" +
                "Are you absolutely sure?",
                "Final Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE
            );
            
            if (confirmResult == JOptionPane.YES_OPTION) {
                boolean success = shopManager.deleteShop(shop.getId());
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Shop '" + shop.getName() + "' has been deleted successfully!",
                        "Shop Deleted",
                        JOptionPane.INFORMATION_MESSAGE);
                    refreshShops(onShopSelected);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to delete shop. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void handleClearAllShops(java.util.function.Consumer<Shop> onShopSelected) {
        int shopCount = shopManager.getShopCount(currentUser.getId());
        
        if (shopCount == 0) {
            JOptionPane.showMessageDialog(this,
                "You don't have any shops to delete.",
                "No Shops",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Show warning dialog
        int result = JOptionPane.showConfirmDialog(
            this,
            "⚠️ DANGER: Clear All Shops ⚠️\n\n" +
            "You are about to delete ALL " + shopCount + " shop(s)!\n\n" +
            "This will permanently delete:\n" +
            "• All your shops (" + shopCount + " shops)\n" +
            "• All products in all shops\n" +
            "• All associated data\n\n" +
            "This action CANNOT be undone!",
            "Clear All Shops - Warning",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            // Final confirmation
            int confirmResult = JOptionPane.showConfirmDialog(
                this,
                "🚨 FINAL WARNING 🚨\n\n" +
                "You are about to PERMANENTLY DELETE all " + shopCount + " shop(s)!\n" +
                "This will erase EVERYTHING!\n\n" +
                "Type-to-confirm: Are you ABSOLUTELY CERTAIN?",
                "Final Confirmation - Clear All",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE
            );
            
            if (confirmResult == JOptionPane.YES_OPTION) {
                boolean success = shopManager.deleteAllShops(currentUser.getId());
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "All shops have been deleted successfully!",
                        "Shops Cleared",
                        JOptionPane.INFORMATION_MESSAGE);
                    refreshShops(onShopSelected);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to clear shops. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    public void updateTheme() {
        boolean isDark = UIManager.getLookAndFeel().getName().contains("Dark");
        if (isDark) {
            setBackground(new Color(45, 45, 45));
        } else {
            setBackground(new Color(248, 249, 250));
        }
        revalidate();
        repaint();
    }
}

