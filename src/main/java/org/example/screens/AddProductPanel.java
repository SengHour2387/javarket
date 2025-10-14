package org.example.screens;

import org.example.DatabaseConnector;
import org.example.ShopManager;
import org.example.models.Shop;
import org.example.models.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AddProductPanel extends JPanel {
    private User currentUser;
    private Shop userShop;
    private DatabaseConnector connector;
    private ShopManager shopManager;
    private Runnable onProductAdded;
    
    private JTextField productNameField;
    private JTextArea descriptionArea;
    private JTextField priceField;
    private JTextField stockField;
    private JTextField imageUrlField;
    private JButton addProductButton;
    
    // Constructor with specific shop
    public AddProductPanel(User currentUser, Shop shop, Runnable onProductAdded) {
        this.currentUser = currentUser;
        this.userShop = shop;
        this.connector = new DatabaseConnector();
        this.shopManager = new ShopManager();
        this.onProductAdded = onProductAdded;
        
        setLayout(new BorderLayout());
        initComponents();
    }
    
    // Legacy constructor for backward compatibility
    public AddProductPanel(User currentUser, Runnable onProductAdded) {
        this.currentUser = currentUser;
        this.connector = new DatabaseConnector();
        this.shopManager = new ShopManager();
        this.onProductAdded = onProductAdded;
        
        // Get user's shop (first shop)
        this.userShop = shopManager.getShopByOwnerId(currentUser.getId());
        
        setLayout(new BorderLayout());
        initComponents();
    }
    
    private void initComponents() {
        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        
        // Title
        JLabel titleLabel = new JLabel("➕ Add New Product");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 28f));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        if (userShop != null) {
            JLabel shopLabel = new JLabel("Shop: " + userShop.getName());
            shopLabel.setFont(shopLabel.getFont().deriveFont(Font.PLAIN, 14f));
            shopLabel.setForeground(Color.GRAY);
            shopLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(shopLabel);
        }
        
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Product Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Product Name *"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        productNameField = new JTextField(20);
        productNameField.setFont(productNameField.getFont().deriveFont(14f));
        formPanel.add(productNameField, gbc);
        
        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTH;
        formPanel.add(new JLabel("Description"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.CENTER;
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setFont(descriptionArea.getFont().deriveFont(14f));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        formPanel.add(descScroll, gbc);
        
        // Price
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Price ($) *"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        priceField = new JTextField(20);
        priceField.setFont(priceField.getFont().deriveFont(14f));
        formPanel.add(priceField, gbc);
        
        // Stock
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Stock Quantity *"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        stockField = new JTextField(20);
        stockField.setFont(stockField.getFont().deriveFont(14f));
        formPanel.add(stockField, gbc);
        
        // Image URL
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Image URL"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        imageUrlField = new JTextField(20);
        imageUrlField.setFont(imageUrlField.getFont().deriveFont(14f));
        formPanel.add(imageUrlField, gbc);
        
        contentPanel.add(formPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Add Product button
        addProductButton = new JButton("Add Product");
        addProductButton.setFont(addProductButton.getFont().deriveFont(Font.BOLD, 16f));
        addProductButton.setBackground(new Color(40, 167, 69));
        addProductButton.setForeground(Color.WHITE);
        addProductButton.setFocusPainted(false);
        addProductButton.setBorderPainted(false);
        addProductButton.setOpaque(true);
        addProductButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addProductButton.setPreferredSize(new Dimension(200, 45));
        addProductButton.setMaximumSize(new Dimension(200, 45));
        addProductButton.addActionListener(e -> handleAddProduct());
        
        contentPanel.add(addProductButton);
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void handleAddProduct() {
        // Validate inputs
        String productName = productNameField.getText().trim();
        String priceText = priceField.getText().trim();
        String stockText = stockField.getText().trim();
        
        if (productName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Product name is required!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid price!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int stock;
        try {
            stock = Integer.parseInt(stockText);
            if (stock < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid stock quantity!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Add product to database
        try {
            String description = descriptionArea.getText().trim();
            String imageUrl = imageUrlField.getText().trim();
            
            // Insert product
            connector.runCUD(
                "INSERT INTO products_tbl (name, description, price, image, stock, seller_id) VALUES (?, ?, ?, ?, ?, ?)",
                productName,
                description,
                price,
                imageUrl.isEmpty() ? null : imageUrl,
                stock,
                currentUser.getId()
            );
            
            // Get the last inserted product ID
            var rs = connector.runSelect("SELECT last_insert_rowid() as id");
            if (rs.next()) {
                int productId = rs.getInt("id");
                
                if (userShop != null) {
                    System.out.println("✅ Linking product " + productId + " to shop " + userShop.getId() + " (" + userShop.getName() + ")");
                    // Link product to shop
                    boolean linked = shopManager.addProductToShop(userShop.getId(), productId);
                    if (linked) {
                        System.out.println("✅ Product successfully linked to shop!");
                    } else {
                        System.err.println("❌ Failed to link product to shop!");
                    }
                } else {
                    System.err.println("❌ ERROR: userShop is null! Cannot link product to shop.");
                }
            }
            
            JOptionPane.showMessageDialog(this,
                "Product added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear form
            clearForm();
            
            // Notify parent
            if (onProductAdded != null) {
                onProductAdded.run();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Failed to add product: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        productNameField.setText("");
        descriptionArea.setText("");
        priceField.setText("");
        stockField.setText("");
        imageUrlField.setText("");
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

