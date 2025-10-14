package org.example.screens;

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
        
        JLabel shopNameLabel = new JLabel("🏪 " + currentShop.getName());
        shopNameLabel.setFont(shopNameLabel.getFont().deriveFont(Font.BOLD, 28f));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton addProductBtn = new JButton("➕ Add Product");
        addProductBtn.setFont(addProductBtn.getFont().deriveFont(Font.BOLD, 14f));
        addProductBtn.setBackground(new Color(40, 167, 69));
        addProductBtn.setForeground(Color.WHITE);
        addProductBtn.setFocusPainted(false);
        addProductBtn.setBorderPainted(false);
        addProductBtn.setOpaque(true);
        addProductBtn.setPreferredSize(new Dimension(150, 40));
        addProductBtn.addActionListener(e -> cardLayout.show(mainPanel, ADD_PRODUCT_CARD));
        
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
        
        if (products.isEmpty()) {
            JLabel emptyLabel = new JLabel("No products yet. Click 'Add Product' to start selling!");
            emptyLabel.setFont(emptyLabel.getFont().deriveFont(Font.ITALIC, 14f));
            emptyLabel.setForeground(Color.GRAY);
            productsPanel.add(emptyLabel);
        } else {
            for (Prodcut product : products) {
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
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        // Product info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 16f));
        
        JLabel priceLabel = new JLabel(String.format("$%.2f", product.getPrice()));
        priceLabel.setForeground(new Color(40, 167, 69));
        priceLabel.setFont(priceLabel.getFont().deriveFont(Font.BOLD, 14f));
        
        JLabel stockLabel = new JLabel("Stock: " + product.getStock());
        stockLabel.setForeground(Color.GRAY);
        stockLabel.setFont(stockLabel.getFont().deriveFont(Font.PLAIN, 12f));
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(stockLabel);
        
        // Action buttons
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionsPanel.setOpaque(false);
        
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBackground(new Color(220, 53, 69));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(deleteBtn.getFont().deriveFont(Font.BOLD, 12f));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setOpaque(true);
        deleteBtn.setPreferredSize(new Dimension(90, 35));
        deleteBtn.addActionListener(e -> handleDeleteProduct(product));
        
        actionsPanel.add(deleteBtn);
        
        rowPanel.add(infoPanel, BorderLayout.WEST);
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

