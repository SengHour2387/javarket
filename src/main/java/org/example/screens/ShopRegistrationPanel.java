package org.example.screens;

import org.example.ShopManager;
import org.example.models.Shop;
import org.example.models.User;

import javax.swing.*;
import java.awt.*;

public class ShopRegistrationPanel extends JPanel {
    private User currentUser;
    private ShopManager shopManager;
    private Runnable onShopCreated;
    
    private JTextField shopNameField;
    private JTextField shopTypeField;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextField emailField;
    private JButton createShopButton;
    
    public ShopRegistrationPanel(User currentUser, Runnable onShopCreated) {
        this.currentUser = currentUser;
        this.shopManager = new ShopManager();
        this.onShopCreated = onShopCreated;
        
        setLayout(new BorderLayout());
        initComponents();
    }
    
    private void initComponents() {
        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        
        // Title
        JLabel titleLabel = new JLabel("🏪 Create Your Shop");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 28f));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        JLabel subtitleLabel = new JLabel("Start selling your products today!");
        subtitleLabel.setFont(subtitleLabel.getFont().deriveFont(Font.PLAIN, 14f));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Shop Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Shop Name *"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        shopNameField = new JTextField(20);
        shopNameField.setFont(shopNameField.getFont().deriveFont(14f));
        formPanel.add(shopNameField, gbc);
        
        // Shop Type
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Shop Type"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        shopTypeField = new JTextField(20);
        shopTypeField.setFont(shopTypeField.getFont().deriveFont(14f));
        formPanel.add(shopTypeField, gbc);
        
        // Address
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Address"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        addressField = new JTextField(20);
        addressField.setFont(addressField.getFont().deriveFont(14f));
        formPanel.add(addressField, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Phone"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        phoneField = new JTextField(20);
        phoneField.setFont(phoneField.getFont().deriveFont(14f));
        formPanel.add(phoneField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Email"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        emailField = new JTextField(20);
        emailField.setFont(emailField.getFont().deriveFont(14f));
        formPanel.add(emailField, gbc);
        
        contentPanel.add(formPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Create button
        createShopButton = new JButton("Create Shop");
        createShopButton.setFont(createShopButton.getFont().deriveFont(Font.BOLD, 16f));
        createShopButton.setBackground(new Color(40, 167, 69));
        createShopButton.setForeground(Color.WHITE);
        createShopButton.setFocusPainted(false);
        createShopButton.setBorderPainted(false);
        createShopButton.setOpaque(true);
        createShopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createShopButton.setPreferredSize(new Dimension(200, 45));
        createShopButton.setMaximumSize(new Dimension(200, 45));
        createShopButton.addActionListener(e -> handleCreateShop());
        
        contentPanel.add(createShopButton);
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void handleCreateShop() {
        // Validate inputs
        String shopName = shopNameField.getText().trim();
        if (shopName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Shop name is required!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create shop object
        Shop shop = new Shop();
        shop.setName(shopName);
        shop.setOwnerId(currentUser.getId());
        shop.setType(shopTypeField.getText().trim());
        shop.setAddress(addressField.getText().trim());
        shop.setPhone(phoneField.getText().trim());
        shop.setEmail(emailField.getText().trim());
        shop.setStatus("active");
        
        // Save to database
        boolean success = shopManager.createShop(shop);
        
        if (success) {
            // Clear form first
            clearForm();
            
            // Show success message
            JOptionPane.showMessageDialog(this,
                "Shop created successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Notify parent to refresh and switch view
            if (onShopCreated != null) {
                SwingUtilities.invokeLater(() -> onShopCreated.run());
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to create shop. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        shopNameField.setText("");
        shopTypeField.setText("");
        addressField.setText("");
        phoneField.setText("");
        emailField.setText("");
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

