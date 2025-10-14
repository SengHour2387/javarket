package org.example.screens;

import org.example.DatabaseConnector;
import org.example.ShopManager;
import org.example.models.Prodcut;
import org.example.models.Shop;
import org.example.models.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MyShopPanel extends JPanel {
    private User currentUser;
    private Shop currentShop;  // Currently selected shop
    private ShopManager shopManager;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private MyShopsListPanel shopsListPanel;
    private JPanel shopOverviewPanel;
    private ShopRegistrationPanel shopRegistrationPanel;
    private AddProductPanel addProductPanel;
    
    private static final String SHOPS_LIST_CARD = "shopsList";
    private static final String OVERVIEW_CARD = "overview";
    private static final String REGISTRATION_CARD = "registration";
    private static final String ADD_PRODUCT_CARD = "addProduct";
    
    public MyShopPanel(User currentUser) {
        this.currentUser = currentUser;
        this.shopManager = new ShopManager();
        
        setLayout(new BorderLayout());
        initComponents();
        checkShopStatus();
    }
    
    private void initComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create shops list panel (shows all user's shops)
        shopsListPanel = new MyShopsListPanel(currentUser, this::showShopRegistration, this::onShopSelected);
        mainPanel.add(shopsListPanel, SHOPS_LIST_CARD);
        
        // Create shop registration panel
        shopRegistrationPanel = new ShopRegistrationPanel(currentUser, this::onShopCreated);
        mainPanel.add(shopRegistrationPanel, REGISTRATION_CARD);
        
        // Create overview panel (will be populated when shop is selected)
        shopOverviewPanel = new JPanel(new BorderLayout());
        mainPanel.add(shopOverviewPanel, OVERVIEW_CARD);
        
        // Create add product panel
        addProductPanel = new AddProductPanel(currentUser, this::onProductAdded);
        mainPanel.add(addProductPanel, ADD_PRODUCT_CARD);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void checkShopStatus() {
        System.out.println("Checking shop status for user ID: " + currentUser.getId());
        int shopCount = shopManager.getShopCount(currentUser.getId());
        System.out.println("User has " + shopCount + " shop(s)");
        
        // Always show shops list first (it will show "create shop" if no shops exist)
        cardLayout.show(mainPanel, SHOPS_LIST_CARD);
    }
    
    private void showShopRegistration() {
        cardLayout.show(mainPanel, REGISTRATION_CARD);
    }
    
    private void onShopSelected(Shop shop) {
        System.out.println("Shop selected: " + shop.getName());
        this.currentShop = shop;
        buildShopOverview();
        cardLayout.show(mainPanel, OVERVIEW_CARD);
    }
    
    private void buildShopOverview() {
        shopOverviewPanel.removeAll();
        
        // Back button + Header
        JPanel topPanel = new JPanel(new BorderLayout());
        
        JButton backBtn = new JButton("← Back to My Shops");
        backBtn.setFont(backBtn.getFont().deriveFont(Font.PLAIN, 14f));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.addActionListener(e -> {
            shopsListPanel.refreshShops(this::onShopSelected);
            cardLayout.show(mainPanel, SHOPS_LIST_CARD);
        });
        
        topPanel.add(backBtn, BorderLayout.NORTH);
        
        // Header with shop info
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        headerPanel.setBackground(new Color(248, 249, 250));
        
        // Shop name with status indicator
        boolean isActive = currentShop.getStatus().equals("active");
        String statusEmoji = isActive ? "🟢" : "🔴";
        JLabel shopNameLabel = new JLabel("🏪 " + currentShop.getName() + " " + statusEmoji);
        shopNameLabel.setFont(shopNameLabel.getFont().deriveFont(Font.BOLD, 28f));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        // Status Toggle Button
        JButton toggleStatusBtn = new JButton(isActive ? "Close Shop" : "Open Shop");
        toggleStatusBtn.setFont(toggleStatusBtn.getFont().deriveFont(Font.BOLD, 13f));
        toggleStatusBtn.setBackground(isActive ? new Color(255, 193, 7) : new Color(40, 167, 69));
        toggleStatusBtn.setForeground(Color.WHITE);
        toggleStatusBtn.setFocusPainted(false);
        toggleStatusBtn.setBorderPainted(false);
        toggleStatusBtn.setOpaque(true);
        toggleStatusBtn.setPreferredSize(new Dimension(130, 40));
        toggleStatusBtn.addActionListener(e -> handleToggleShopStatus());
        
        JButton addProductBtn = new JButton("➕ Add Product");
        addProductBtn.setFont(addProductBtn.getFont().deriveFont(Font.BOLD, 14f));
        addProductBtn.setBackground(new Color(40, 167, 69));
        addProductBtn.setForeground(Color.WHITE);
        addProductBtn.setFocusPainted(false);
        addProductBtn.setBorderPainted(false);
        addProductBtn.setOpaque(true);
        addProductBtn.setPreferredSize(new Dimension(150, 40));
        addProductBtn.addActionListener(e -> showAddProductDialog());
        
        buttonPanel.add(toggleStatusBtn);
        buttonPanel.add(addProductBtn);
        
        headerPanel.add(shopNameLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Products list
        JPanel productsPanel = new JPanel();
        productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));
        productsPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel productsTitle = new JLabel("Your Products");
        productsTitle.setFont(productsTitle.getFont().deriveFont(Font.BOLD, 20f));
        productsPanel.add(productsTitle);
        productsPanel.add(Box.createVerticalStrut(20));
        
        // Load and display products
        List<Prodcut> products = shopManager.getShopProducts(currentShop.getId());
        System.out.println("🔍 Loading products for shop " + currentShop.getId() + " (" + currentShop.getName() + ")");
        System.out.println("📦 Found " + products.size() + " products");
        
        if (products.isEmpty()) {
            JLabel emptyLabel = new JLabel("No products yet. Click 'Add Product' to start selling!");
            emptyLabel.setFont(emptyLabel.getFont().deriveFont(Font.ITALIC, 14f));
            emptyLabel.setForeground(Color.GRAY);
            productsPanel.add(emptyLabel);
        } else {
            for (Prodcut product : products) {
                System.out.println("  - " + product.getName() + " (ID: " + product.getId() + ")");
                productsPanel.add(createProductRow(product));
                productsPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        
        topPanel.add(headerPanel, BorderLayout.CENTER);
        
        shopOverviewPanel.add(topPanel, BorderLayout.NORTH);
        shopOverviewPanel.add(scrollPane, BorderLayout.CENTER);
        
        shopOverviewPanel.revalidate();
        shopOverviewPanel.repaint();
    }
    
    private JPanel createProductRow(Prodcut product) {
        JPanel rowPanel = new JPanel(new BorderLayout(10, 0));
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Product info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 16f));
        
        String description = product.getDescription();
        if (description != null && !description.isEmpty()) {
            JLabel descLabel = new JLabel(description.length() > 60 ? description.substring(0, 60) + "..." : description);
            descLabel.setForeground(Color.GRAY);
            descLabel.setFont(descLabel.getFont().deriveFont(Font.PLAIN, 12f));
            infoPanel.add(nameLabel);
            infoPanel.add(Box.createVerticalStrut(5));
            infoPanel.add(descLabel);
        } else {
            infoPanel.add(nameLabel);
            infoPanel.add(Box.createVerticalStrut(10));
        }
        
        JLabel priceLabel = new JLabel(String.format("Price: $%.2f | Stock: %d", product.getPrice(), product.getStock()));
        priceLabel.setForeground(new Color(40, 167, 69));
        priceLabel.setFont(priceLabel.getFont().deriveFont(Font.BOLD, 14f));
        
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);
        
        // Action buttons
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setOpaque(false);
        
        JButton editBtn = new JButton("✏️ Edit");
        editBtn.setBackground(new Color(0, 123, 255));
        editBtn.setForeground(Color.WHITE);
        editBtn.setFont(editBtn.getFont().deriveFont(Font.BOLD, 12f));
        editBtn.setFocusPainted(false);
        editBtn.setBorderPainted(false);
        editBtn.setOpaque(true);
        editBtn.setPreferredSize(new Dimension(100, 35));
        editBtn.addActionListener(e -> showEditProductDialog(product));
        
        JButton deleteBtn = new JButton("🗑️ Delete");
        deleteBtn.setBackground(new Color(220, 53, 69));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(deleteBtn.getFont().deriveFont(Font.BOLD, 12f));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setOpaque(true);
        deleteBtn.setPreferredSize(new Dimension(100, 35));
        deleteBtn.addActionListener(e -> handleDeleteProduct(product));
        
        actionsPanel.add(editBtn);
        actionsPanel.add(deleteBtn);
        
        rowPanel.add(infoPanel, BorderLayout.CENTER);
        rowPanel.add(actionsPanel, BorderLayout.EAST);
        
        return rowPanel;
    }
    
    private void handleDeleteProduct(Prodcut product) {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete '" + product.getName() + "'?",
            "Delete Product",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            boolean success = shopManager.removeProductFromShop(currentShop.getId(), product.getId());
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Product deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                buildShopOverview();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to delete product.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void onShopCreated() {
        // Refresh shop status and show overview
        System.out.println("Shop created callback triggered - refreshing...");
        
        // Use SwingUtilities.invokeLater to refresh on EDT without blocking
        SwingUtilities.invokeLater(() -> {
            // Create a fresh ShopManager instance to get latest data
            shopManager = new ShopManager();
            // Refresh shops list and go back to it
            shopsListPanel.refreshShops(this::onShopSelected);
            cardLayout.show(mainPanel, SHOPS_LIST_CARD);
        });
    }
    
    public void refreshShopStatus() {
        // Public method to force refresh from parent
        shopManager = new ShopManager();
        checkShopStatus();
    }
    
    private void onProductAdded() {
        // Go back to overview and refresh
        buildShopOverview();
        cardLayout.show(mainPanel, OVERVIEW_CARD);
    }
    
    private void showAddProductDialog() {
        // Create a dialog to show the add product panel
        JDialog dialog = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this), "Add Product to " + currentShop.getName(), true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        
        // Create add product panel with specific shop and callback to close dialog and refresh
        AddProductPanel addPanel = new AddProductPanel(currentUser, currentShop, () -> {
            System.out.println("🔄 Product added - closing dialog and refreshing shop overview...");
            dialog.dispose();
            buildShopOverview(); // Refresh to show new product
            System.out.println("✅ Shop overview refreshed");
        });
        
        dialog.add(addPanel);
        dialog.setVisible(true);
    }
    
    private void showEditProductDialog(Prodcut product) {
        JDialog dialog = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this), "Edit Product", true);
        dialog.setSize(600, 600);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Title
        JLabel titleLabel = new JLabel("✏️ Edit Product");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Product Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Product Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JTextField nameField = new JTextField(product.getName(), 20);
        nameField.setFont(nameField.getFont().deriveFont(14f));
        formPanel.add(nameField, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTH;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.CENTER;
        JTextArea descArea = new JTextArea(product.getDescription() != null ? product.getDescription() : "", 3, 20);
        descArea.setFont(descArea.getFont().deriveFont(14f));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        formPanel.add(descScroll, gbc);
        
        // Price
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Price ($):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JTextField priceField = new JTextField(String.valueOf(product.getPrice()), 20);
        priceField.setFont(priceField.getFont().deriveFont(14f));
        formPanel.add(priceField, gbc);
        
        // Stock
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Stock Quantity:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JTextField stockField = new JTextField(String.valueOf(product.getStock()), 20);
        stockField.setFont(stockField.getFont().deriveFont(14f));
        formPanel.add(stockField, gbc);
        
        // Discount (future feature - placeholder)
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Discount (%):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JTextField discountField = new JTextField("0", 20);
        discountField.setFont(discountField.getFont().deriveFont(14f));
        formPanel.add(discountField, gbc);
        
        // Image URL
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Image URL:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JTextField imageField = new JTextField(product.getImage() != null ? product.getImage() : "", 20);
        imageField.setFont(imageField.getFont().deriveFont(14f));
        formPanel.add(imageField, gbc);
        
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton saveBtn = new JButton("💾 Save Changes");
        saveBtn.setFont(saveBtn.getFont().deriveFont(Font.BOLD, 14f));
        saveBtn.setBackground(new Color(40, 167, 69));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setPreferredSize(new Dimension(160, 40));
        saveBtn.addActionListener(e -> {
            // Validate and update
            try {
                String name = nameField.getText().trim();
                String desc = descArea.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int stock = Integer.parseInt(stockField.getText().trim());
                String image = imageField.getText().trim();
                
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Product name is required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (price < 0 || stock < 0) {
                    JOptionPane.showMessageDialog(dialog, "Price and stock must be positive!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Update product in database
                DatabaseConnector connector = new DatabaseConnector();
                connector.runCUD(
                    "UPDATE products_tbl SET name = ?, description = ?, price = ?, stock = ?, image = ? WHERE id = ?",
                    name, desc, price, stock, image.isEmpty() ? null : image, product.getId()
                );
                
                JOptionPane.showMessageDialog(dialog, "Product updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                buildShopOverview(); // Refresh
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid price or stock value!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Failed to update product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(cancelBtn.getFont().deriveFont(Font.BOLD, 14f));
        cancelBtn.setBackground(new Color(108, 117, 125));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setPreferredSize(new Dimension(100, 40));
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonsPanel.add(saveBtn);
        buttonsPanel.add(cancelBtn);
        mainPanel.add(buttonsPanel);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        dialog.add(scrollPane);
        dialog.setVisible(true);
    }
    
    private void handleToggleShopStatus() {
        boolean isCurrentlyActive = currentShop.getStatus().equals("active");
        String newStatus = isCurrentlyActive ? "inactive" : "active";
        String action = isCurrentlyActive ? "close" : "open";
        
        int result = JOptionPane.showConfirmDialog(
            this,
            "Do you want to " + action + " '" + currentShop.getName() + "'?\n\n" +
            (isCurrentlyActive ? 
                "⚠️ Closing your shop will:\n" +
                "• Make it invisible to customers\n" +
                "• Stop accepting new orders\n" +
                "• Show as 🔴 CLOSED" :
                "✅ Opening your shop will:\n" +
                "• Make it visible to customers\n" +
                "• Start accepting orders\n" +
                "• Show as 🟢 OPEN"),
            "Toggle Shop Status",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            boolean success = shopManager.toggleShopStatus(currentShop.getId(), newStatus);
            if (success) {
                currentShop.setStatus(newStatus); // Update local object
                JOptionPane.showMessageDialog(this,
                    "Shop '" + currentShop.getName() + "' is now " + 
                    (newStatus.equals("active") ? "🟢 OPEN" : "🔴 CLOSED") + "!",
                    "Status Updated",
                    JOptionPane.INFORMATION_MESSAGE);
                // Refresh the overview to show new status
                buildShopOverview();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to update shop status. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
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
        
        if (shopRegistrationPanel != null) {
            shopRegistrationPanel.updateTheme();
        }
        if (addProductPanel != null) {
            addProductPanel.updateTheme();
        }
        
        revalidate();
        repaint();
    }
}

