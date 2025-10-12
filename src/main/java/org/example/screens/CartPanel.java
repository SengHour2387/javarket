package org.example.screens;

import org.example.CartManager;
import org.example.models.Prodcut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.formdev.flatlaf.FlatClientProperties;

public class CartPanel extends JPanel {
    
    private JPanel cartItemsPanel;
    private JScrollPane scrollPane;
    private JLabel totalLabel;
    private JButton checkoutButton;
    private JButton clearCartButton;
    private CartManager cartManager;
    
    public CartPanel() {
        super();
        setLayout(new BorderLayout());
        cartManager = CartManager.getInstance();
        initComponents();
        loadCartItems();
    }
    
    private void initComponents() {
        // Header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel titleLabel = new JLabel("🛒 Shopping Cart");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));
        titleLabel.setForeground(new Color(52, 58, 64));
        headerPanel.add(titleLabel);
        
        // Cart items panel
        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        scrollPane = new JScrollPane(cartItemsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Bottom panel with total and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(255, 255, 255));
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Total and buttons panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(new Color(255, 255, 255));
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 20f));
        totalLabel.setForeground(new Color(40, 167, 69));
        
        checkoutButton = new JButton("💳 Checkout");
        checkoutButton.setFont(checkoutButton.getFont().deriveFont(Font.BOLD, 14f));
        checkoutButton.setBackground(new Color(40, 167, 69));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFocusPainted(false);
        checkoutButton.setBorderPainted(false);
        checkoutButton.setOpaque(true);
        checkoutButton.setPreferredSize(new Dimension(120, 45));
        checkoutButton.setMinimumSize(new Dimension(120, 45));
        checkoutButton.setMaximumSize(new Dimension(120, 45));
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkout();
            }
        });
        
        clearCartButton = new JButton("🗑️ Clear Cart");
        clearCartButton.setFont(clearCartButton.getFont().deriveFont(Font.BOLD, 14f));
        clearCartButton.setBackground(new Color(220, 53, 69));
        clearCartButton.setForeground(Color.WHITE);
        clearCartButton.setFocusPainted(false);
        clearCartButton.setBorderPainted(false);
        clearCartButton.setOpaque(true);
        clearCartButton.setPreferredSize(new Dimension(120, 45));
        clearCartButton.setMinimumSize(new Dimension(120, 45));
        clearCartButton.setMaximumSize(new Dimension(120, 45));
        clearCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearCart();
            }
        });
        
        totalPanel.add(totalLabel);
        totalPanel.add(Box.createHorizontalStrut(10));
        totalPanel.add(clearCartButton);
        totalPanel.add(Box.createHorizontalStrut(5));
        totalPanel.add(checkoutButton);
        
        bottomPanel.add(totalPanel, BorderLayout.CENTER);
        
        // Add components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void loadCartItems() {
        cartItemsPanel.removeAll();
        
        if (cartManager.isEmpty()) {
            // Add empty cart message
            JLabel emptyCartLabel = new JLabel("Your cart is empty");
            emptyCartLabel.setFont(emptyCartLabel.getFont().deriveFont(Font.ITALIC, 14f));
            emptyCartLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyCartLabel.setForeground(Color.GRAY);
            cartItemsPanel.add(emptyCartLabel);
        } else {
            // Add cart items
            for (CartManager.CartItem item : cartManager.getCartItems()) {
                cartItemsPanel.add(createCartItemPanel(item));
                cartItemsPanel.add(Box.createVerticalStrut(5));
            }
        }
        
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
        updateTotal();
    }
    
    public void refreshCart() {
        loadCartItems();
    }
    
    private JPanel createCartItemPanel(CartManager.CartItem cartItem) {
        Prodcut product = cartItem.getProduct();
        int quantity = cartItem.getQuantity();
        double price = product.getPrice();
        String productName = product.getName();
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        // Product info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        JLabel nameLabel = new JLabel(productName);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14f));
        nameLabel.setForeground(new Color(52, 58, 64));
        
        JLabel priceLabel = new JLabel(String.format("$%.2f", price));
        priceLabel.setForeground(new Color(40, 167, 69));
        priceLabel.setFont(priceLabel.getFont().deriveFont(Font.BOLD, 13f));
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(2));
        infoPanel.add(priceLabel);
        
        // Quantity and controls panel
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton minusButton = new JButton("-");
        minusButton.setPreferredSize(new Dimension(30, 30));
        minusButton.setFont(minusButton.getFont().deriveFont(Font.BOLD, 12f));
        minusButton.setBackground(new Color(108, 117, 125));
        minusButton.setForeground(Color.WHITE);
        minusButton.setFocusPainted(false);
        minusButton.setBorderPainted(false);
        minusButton.setOpaque(true);
        minusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cartManager.updateQuantity(product.getId(), quantity - 1);
                refreshCart();
            }
        });
        
        JLabel quantityLabel = new JLabel(String.valueOf(quantity));
        quantityLabel.setFont(quantityLabel.getFont().deriveFont(Font.BOLD, 12f));
        quantityLabel.setPreferredSize(new Dimension(40, 30));
        quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        quantityLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        JButton plusButton = new JButton("+");
        plusButton.setPreferredSize(new Dimension(30, 30));
        plusButton.setFont(plusButton.getFont().deriveFont(Font.BOLD, 12f));
        plusButton.setBackground(new Color(40, 167, 69));
        plusButton.setForeground(Color.WHITE);
        plusButton.setFocusPainted(false);
        plusButton.setBorderPainted(false);
        plusButton.setOpaque(true);
        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cartManager.updateQuantity(product.getId(), quantity + 1);
                refreshCart();
            }
        });
        
        JButton removeButton = new JButton("Remove");
        removeButton.setBackground(new Color(220, 53, 69));
        removeButton.setForeground(Color.WHITE);
        removeButton.setFont(removeButton.getFont().deriveFont(Font.BOLD, 10f));
        removeButton.setFocusPainted(false);
        removeButton.setBorderPainted(false);
        removeButton.setOpaque(true);
        removeButton.setPreferredSize(new Dimension(70, 30));
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cartManager.removeFromCart(product.getId());
                refreshCart();
            }
        });
        
        controlsPanel.add(minusButton);
        controlsPanel.add(quantityLabel);
        controlsPanel.add(plusButton);
        controlsPanel.add(Box.createHorizontalStrut(10));
        controlsPanel.add(removeButton);
        
        itemPanel.add(infoPanel, BorderLayout.WEST);
        itemPanel.add(controlsPanel, BorderLayout.EAST);
        
        return itemPanel;
    }
    
    private void updateTotal() {
        double total = cartManager.getTotalPrice();
        totalLabel.setText(String.format("Total: $%.2f", total));
    }
    
    private void checkout() {
        if (cartManager.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!", "Checkout", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(
            this,
            "Proceed to checkout with total: " + String.format("$%.2f", cartManager.getTotalPrice()) + "?",
            "Confirm Checkout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (result == JOptionPane.YES_OPTION) {
            // Here you would integrate with your order system
            JOptionPane.showMessageDialog(this, "Order placed successfully!", "Checkout Complete", JOptionPane.INFORMATION_MESSAGE);
            cartManager.clearCart();
            refreshCart();
        }
    }
    
    public void clearCart() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to clear your cart?",
            "Clear Cart",
            JOptionPane.YES_NO_OPTION
        );
        
        if (result == JOptionPane.YES_OPTION) {
            cartManager.clearCart();
            refreshCart();
        }
    }
    
    public void updateTheme() {
        // Update the main panel background
        if (UIManager.getLookAndFeel().getName().contains("Dark")) {
            setBackground(new Color(45, 45, 45));
        } else {
            setBackground(new Color(248, 249, 250));
        }
        
        // Reapply button styling after theme change
        reapplyButtonStyling();
        
        // Revalidate and repaint
        revalidate();
        repaint();
    }
    
    private void reapplyButtonStyling() {
        // Restyle main buttons
        if (checkoutButton != null) {
            checkoutButton.setBackground(new Color(40, 167, 69));
            checkoutButton.setForeground(Color.WHITE);
            checkoutButton.setFocusPainted(false);
            checkoutButton.setBorderPainted(false);
            checkoutButton.setOpaque(true);
        }
        
        if (clearCartButton != null) {
            clearCartButton.setBackground(new Color(220, 53, 69));
            clearCartButton.setForeground(Color.WHITE);
            clearCartButton.setFocusPainted(false);
            clearCartButton.setBorderPainted(false);
            clearCartButton.setOpaque(true);
        }
        
        // Restyle buttons in cart items
        Component[] components = cartItemsPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                restyleButtonsInCartItem((JPanel) component);
            }
        }
    }
    
    private void restyleButtonsInCartItem(JPanel itemPanel) {
        Component[] components = itemPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                String text = button.getText();
                if ("-".equals(text)) {
                    button.setBackground(new Color(108, 117, 125));
                    button.setForeground(Color.WHITE);
                    button.setFocusPainted(false);
                    button.setBorderPainted(false);
                    button.setOpaque(true);
                } else if ("+".equals(text)) {
                    button.setBackground(new Color(40, 167, 69));
                    button.setForeground(Color.WHITE);
                    button.setFocusPainted(false);
                    button.setBorderPainted(false);
                    button.setOpaque(true);
                } else if ("Remove".equals(text)) {
                    button.setBackground(new Color(220, 53, 69));
                    button.setForeground(Color.WHITE);
                    button.setFocusPainted(false);
                    button.setBorderPainted(false);
                    button.setOpaque(true);
                }
            } else if (component instanceof JPanel) {
                restyleButtonsInCartItem((JPanel) component);
            }
        }
    }
}
