package org.example.screens;

import org.example.SimpleProductManager;
import org.example.CartManager;
import org.example.models.Prodcut;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import com.formdev.flatlaf.FlatClientProperties;

// Custom responsive grid layout that adapts to container width
class ResponsiveGridLayout implements LayoutManager2 {
    private int hgap, vgap;
    private int minColumns = 1;
    private int maxColumns = 6;
    // Match the actual card size used below to avoid clipping and misalignment
    private int cardWidth = 220;
    private int cardHeight = 320;

    public ResponsiveGridLayout(int hgap, int vgap) {
        this.hgap = hgap;
        this.vgap = vgap;
    }

    private boolean isValidUrl(String s) {
        try {
            new java.net.URL(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Build a simple product-image URL from product name using dummyimage service
    private String inferImageUrl(Prodcut product) {
        try {
            String name = product.getName();
            if (name == null || name.isBlank()) return null;
            String text = java.net.URLEncoder.encode(name, java.nio.charset.StandardCharsets.UTF_8.name());
            // 300x240 placeholder with product name text
            return "https://dummyimage.com/300x240/eeeeee/555555.png&text=" + text;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {}

    @Override
    public void addLayoutComponent(String name, Component comp) {}

    @Override
    public void removeLayoutComponent(Component comp) {}

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return calculateLayoutSize(parent, true);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return calculateLayoutSize(parent, false);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }

    @Override
    public void invalidateLayout(Container target) {}

    @Override
    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int availableWidth = parent.getWidth() - insets.left - insets.right;
        
        int columns = calculateColumns(availableWidth);
        
        // Keep cards at their designed width; reduce number of columns instead of shrinking cards
        int cardWidth = this.cardWidth;
        int cardHeight = this.cardHeight;
        int totalRowWidth = columns * cardWidth + (columns - 1) * hgap;
        int startX = insets.left + Math.max(0, (availableWidth - totalRowWidth) / 2);
        
        for (int i = 0; i < parent.getComponentCount(); i++) {
            Component comp = parent.getComponent(i);
            int row = i / columns;
            int col = i % columns;
            
            int x = startX + col * (cardWidth + hgap);
            int y = insets.top + row * (cardHeight + vgap);
            
            comp.setBounds(x, y, cardWidth, cardHeight);
        }
    }

    private int calculateColumns(int availableWidth) {
        int columns = Math.max(minColumns, (availableWidth + hgap) / (cardWidth + hgap));
        return Math.min(columns, maxColumns);
    }

    private Dimension calculateLayoutSize(Container parent, boolean preferred) {
        Insets insets = parent.getInsets();
        int availableWidth = parent.getWidth() - insets.left - insets.right;
        
        if (availableWidth <= 0) {
            availableWidth = 800; // Default width for calculation
        }
        
        int columns = calculateColumns(availableWidth);
        int rows = (int) Math.ceil((double) parent.getComponentCount() / columns);
        
        int width = columns * this.cardWidth + (columns - 1) * hgap + insets.left + insets.right;
        int height = rows * cardHeight + (rows - 1) * vgap + insets.top + insets.bottom;
        
        return new Dimension(width, height);
    }
}

public class ShopPanel extends JPanel {

    private final JPanel productsGrid;
    private final JScrollPane scrollPane;
    private CartManager cartManager;
    private final JLabel loadingLabel = new JLabel("Loading products…", SwingConstants.CENTER);
    private java.util.List<JButton> addToCartButtons = new java.util.ArrayList<>();

    // Helpers for remote product images
    private boolean isValidUrl(String s) {
        try {
            new java.net.URL(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String inferImageUrl(Prodcut product) {
        try {
            String name = product.getName();
            if (name == null || name.isBlank()) return null;
            String text = java.net.URLEncoder.encode(name, java.nio.charset.StandardCharsets.UTF_8.name());
            return "https://dummyimage.com/300x240/eeeeee/555555.png&text=" + text;
        } catch (Exception e) {
            return null;
        }
    }

    public ShopPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));
        cartManager = CartManager.getInstance();

        // Use responsive grid layout that adapts to container width
        productsGrid = new JPanel(new ResponsiveGridLayout(8, 8));
        productsGrid.setBackground(new Color(248, 249, 250));
        productsGrid.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        scrollPane = new JScrollPane(productsGrid);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add component listener for responsive behavior
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                productsGrid.revalidate();
                productsGrid.repaint();
            }
        });

        add(scrollPane, BorderLayout.CENTER);

        // Show loading state first, then load products asynchronously
        showLoadingState();
        loadProductsAsync();
    }

    private void loadProducts() {
        // Synchronous load (kept for potential reuse) - not used on EDT anymore
        SimpleProductManager manager = new SimpleProductManager();
        List<Prodcut> products = manager.getAllProducts();
        manager.close();

        productsGrid.removeAll();
        for (Prodcut product : products) {
            productsGrid.add(createProductCard(product));
        }
        productsGrid.revalidate();
        productsGrid.repaint();
    }

    private void showLoadingState() {
        productsGrid.removeAll();
        JPanel loadingPanel = new JPanel(new BorderLayout());
        loadingLabel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        loadingPanel.add(loadingLabel, BorderLayout.CENTER);
        productsGrid.add(loadingPanel);
        productsGrid.revalidate();
        productsGrid.repaint();
    }

    private void loadProductsAsync() {
        SwingWorker<List<Prodcut>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Prodcut> doInBackground() {
                SimpleProductManager manager = new SimpleProductManager();
                List<Prodcut> products = manager.getAllProducts();
                manager.close();
                return products;
            }

            @Override
            protected void done() {
                try {
                    List<Prodcut> products = get();
                    productsGrid.removeAll();
                    addToCartButtons.clear(); // Clear old button references
                    for (Prodcut product : products) {
                        productsGrid.add(createProductCard(product));
                    }
                    productsGrid.revalidate();
                    productsGrid.repaint();
                } catch (Exception e) {
                    System.err.println("Error loading products: " + e.getMessage());
                    e.printStackTrace();
                    productsGrid.removeAll();
                    JPanel errorPanel = new JPanel(new BorderLayout());
                    JLabel error = new JLabel("Failed to load products", SwingConstants.CENTER);
                    error.setForeground(Color.RED);
                    JLabel detail = new JLabel("Error: " + e.getMessage(), SwingConstants.CENTER);
                    detail.setForeground(Color.GRAY);
                    detail.setFont(detail.getFont().deriveFont(10f));
                    errorPanel.add(error, BorderLayout.CENTER);
                    errorPanel.add(detail, BorderLayout.SOUTH);
                    productsGrid.add(errorPanel);
                    productsGrid.revalidate();
                    productsGrid.repaint();
                }
            }
        };
        worker.execute();
    }

    private JComponent createProductCard(Prodcut product) {
        boolean isDark = UIManager.getLookAndFeel().getName().contains("Dark");
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // FlatLaf rounded styling (Panel does not support borderColor via STYLE)
        if (isDark) {
            card.putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE,
                    "arc:14; background:#3C3C3C" );
            card.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createLineBorder(new java.awt.Color(80,80,80), 1),
                    javax.swing.BorderFactory.createEmptyBorder(20,20,20,20)
            ));
        } else {
            card.putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE,
                    "arc:14; background:#FFFFFF" );
            card.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createLineBorder(new java.awt.Color(230,230,230), 1),
                    javax.swing.BorderFactory.createEmptyBorder(20,20,20,20)
            ));
        }
        
        card.setPreferredSize(new Dimension(220, 320));
        card.setMaximumSize(new Dimension(220, 320));
        card.setMinimumSize(new Dimension(220, 320));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Image (optional)
        JLabel imageLabel = createImageLabel(product.getImage());
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 16f));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (isDark) {
            nameLabel.setForeground(Color.WHITE);
        } else {
            nameLabel.setForeground(new Color(52, 58, 64));
        }


        JLabel productImg = new JLabel();
        productImg.setPreferredSize(new Dimension(100, 80));
        productImg.setOpaque(true);
        productImg.setBackground(new Color(245, 245, 245));
        productImg.setBorder(BorderFactory.createLineBorder(new Color(235, 235, 235)));
        productImg.setHorizontalAlignment(SwingConstants.CENTER);
        productImg.setText("No Image");
        productImg.setForeground(Color.GRAY);
        productImg.setFont(productImg.getFont().deriveFont(10f));
        
        // Try to load image asynchronously with better error handling
        if (true) {
            SwingWorker<ImageIcon, Void> imgWorker = new SwingWorker<>() {
                @Override
                protected ImageIcon doInBackground() {
                    try {
                        String imageStr = product.getImage();
                        if (imageStr == null || imageStr.trim().isEmpty() || imageStr.equals("No Image") || !isValidUrl(imageStr)) {
                            imageStr = inferImageUrl(product);
                        }
                        if (imageStr == null) {
                            return null;
                        }
                        URL imgUrl = new URL(imageStr);
                        java.net.URLConnection conn = imgUrl.openConnection();
                        conn.setConnectTimeout(4000);
                        conn.setReadTimeout(6000);
                        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                        try (java.io.InputStream in = conn.getInputStream()) {
                            BufferedImage image = ImageIO.read(in);
                            if (image != null) {
                                Image resizedImg = image.getScaledInstance(100, 80, Image.SCALE_SMOOTH);
                                return new ImageIcon(resizedImg);
                            }
                        }
                        System.err.println("Failed to load image from URL: " + imgUrl);
                    } catch (MalformedURLException e) {
                        System.err.println("Invalid URL for image: " + product.getImage() + " - " + e.getMessage());
                    } catch (IOException e) {
                        System.err.println("Failed to load image from: " + product.getImage() + " - " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Unexpected error loading image: " + product.getImage() + " - " + e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        ImageIcon scaledIcon = get();
                        if (scaledIcon != null) {
                            productImg.setText("");
                            productImg.setIcon(scaledIcon);
                            productImg.setBackground(Color.WHITE);
                        } else {
                            productImg.setText("Failed to load");
                            productImg.setForeground(Color.RED);
                        }
                    } catch (Exception e) {
                        productImg.setText("Error loading");
                        productImg.setForeground(Color.RED);
                        System.err.println("Error in image loading done(): " + e.getMessage());
                    }
                }
            };
            imgWorker.execute();
        }
        productImg.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel(String.format("$%.2f", product.getPrice()));
        priceLabel.setFont(priceLabel.getFont().deriveFont(Font.BOLD, 18f));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (isDark) {
            priceLabel.setForeground(new Color(76, 175, 80)); // Lighter green for dark theme
        } else {
            priceLabel.setForeground(new Color(40, 167, 69));
        }

        // Truncate description for smaller cards
        String description = product.getDescription();
        if (description.length() > 60) {
            description = description.substring(0, 57) + "...";
        }
        JTextArea descArea = new JTextArea(description);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setOpaque(false);
        descArea.setFont(descArea.getFont().deriveFont(11f));
        descArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (isDark) {
            descArea.setForeground(new Color(180, 180, 180)); // Light gray for dark theme
        } else {
            descArea.setForeground(new Color(108, 117, 125));
        }

        JButton addToCart = new JButton("🛒 Add to Cart");
        addToCart.setFont(addToCart.getFont().deriveFont(Font.BOLD, 13f));
        addToCart.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // FlatLaf rounded green button styling
        addToCart.putClientProperty(com.formdev.flatlaf.FlatClientProperties.BUTTON_TYPE,
                com.formdev.flatlaf.FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        addToCart.putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE,
                "arc:18; background:#28A745; foreground:#FFFFFF; focusWidth:0; innerFocusWidth:0; borderWidth:0;" );
        addToCart.setPreferredSize(new Dimension(160, 40));
        addToCart.setMinimumSize(new Dimension(160, 40));
        addToCart.setMaximumSize(new Dimension(160, 40));
        addToCart.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        // Add action listener to the button
        addToCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart(product);
            }
        });

        // Store button reference for theme updates
        addToCartButtons.add(addToCart);

        card.add(nameLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(productImg);
        card.add(Box.createVerticalStrut(4));
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(descArea);
        card.add(Box.createVerticalStrut(8));
        card.add(addToCart);
        card.add(Box.createVerticalStrut(8));

        return card;
    }

    private JLabel createImageLabel(String imagePathOrUrl) {
        int targetW = 180;
        int targetH = 100;

        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(targetW, targetH));
        label.setOpaque(true);
        label.setBackground(new Color(245, 245, 245));
        label.setBorder(BorderFactory.createLineBorder(new Color(235, 235, 235)));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setText("No Image");
        label.setForeground(Color.GRAY);
        label.setFont(label.getFont().deriveFont(10f));

        if (imagePathOrUrl == null || imagePathOrUrl.trim().isEmpty()) {
            return label;
        }

        String path = imagePathOrUrl.trim();

        SwingWorker<ImageIcon, Void> imgWorker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() {
                try {
                    ImageIcon icon = null;
                    // Try URL
                    try {
                        URL url = new URL(path);
                        BufferedImage image = ImageIO.read(url);
                        icon = new ImageIcon(image);
                    } catch (Exception e) {
                        // Try file
                        File f = new File(path);
                        if (f.exists()) {
                            icon = new ImageIcon(path);
                        } else {
                            // Try classpath
                            URL res = getClass().getResource(path.startsWith("/") ? path : "/" + path);
                            if (res != null) {
                                icon = new ImageIcon(res);
                            }
                        }
                    }
                    if (icon != null && icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
                        Image scaled = icon.getImage().getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
                        return new ImageIcon(scaled);
                    }
                } catch (Exception ignore) {
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    ImageIcon scaledIcon = get();
                    if (scaledIcon != null) {
                        label.setText("");
                        label.setIcon(scaledIcon);
                        label.setBackground(Color.WHITE);
                    }
                } catch (Exception ignore) {
                }
            }
        };
        imgWorker.execute();

        return label;
    }
    
    private void addToCart(Prodcut product) {
        // Ask for quantity
        String quantityStr = JOptionPane.showInputDialog(
            this,
            "Enter quantity for " + product.getName() + ":",
            "Add to Cart",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (quantityStr != null && !quantityStr.trim().isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityStr.trim());
                if (quantity > 0) {
                    cartManager.addToCart(product, quantity);
                    JOptionPane.showMessageDialog(
                        this,
                        quantity + " x " + product.getName() + " added to cart!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Quantity must be greater than 0",
                        "Invalid Quantity",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid number",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    public void updateTheme() {
        System.out.println("ShopPanel updateTheme called. Button count: " + addToCartButtons.size());
        
        // Update the main panel background
        if (UIManager.getLookAndFeel().getName().contains("Dark")) {
            setBackground(new Color(45, 45, 45));
            System.out.println("Set dark background");
        } else {
            setBackground(new Color(248, 249, 250));
            System.out.println("Set light background");
        }
        
        // Refresh all product cards to update their theme
        refreshProductCards();
        
        // Reapply button styling after theme change
        reapplyButtonStyling();
        
        // Revalidate and repaint
        revalidate();
        repaint();
    }
    
    private void refreshProductCards() {
        // Recreate all product cards with new theme
        Component[] components = productsGrid.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel card = (JPanel) component;
                boolean isDark = UIManager.getLookAndFeel().getName().contains("Dark");
                
                // Update card background
                if (isDark) {
                    card.setBackground(new Color(60, 60, 60));
                    card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                    ));
                } else {
                    card.setBackground(Color.WHITE);
                    card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                    ));
                }
                
                // Update text colors in the card
                updateCardTextColors(card, isDark);
            }
        }
    }
    
    private void updateCardTextColors(JPanel card, boolean isDark) {
        Component[] components = card.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                String text = label.getText();
                if (text != null && text.startsWith("$")) {
                    // Price label
                    if (isDark) {
                        label.setForeground(new Color(76, 175, 80));
                    } else {
                        label.setForeground(new Color(40, 167, 69));
                    }
                } else if (text != null && !text.startsWith("🛒")) {
                    // Name label (not button)
                    if (isDark) {
                        label.setForeground(Color.WHITE);
                    } else {
                        label.setForeground(new Color(52, 58, 64));
                    }
                }
            } else if (component instanceof JTextArea) {
                JTextArea textArea = (JTextArea) component;
                if (isDark) {
                    textArea.setForeground(new Color(180, 180, 180));
                } else {
                    textArea.setForeground(new Color(108, 117, 125));
                }
            }
        }
    }
    
    private void reapplyButtonStyling() {
        System.out.println("Reapplying button styling. Found " + addToCartButtons.size() + " buttons");
        
        // Restyle all stored Add to Cart buttons with direct styling
        for (JButton button : addToCartButtons) {
            if (button != null && button.isVisible()) {
                System.out.println("Restyling button: " + button.getText());
                
                // Direct styling to ensure button is always green and visible
                button.setBackground(new Color(40, 167, 69));
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.setBorderPainted(false);
                button.setOpaque(true);
                button.setPreferredSize(new Dimension(160, 40));
                button.setMinimumSize(new Dimension(160, 40));
                button.setMaximumSize(new Dimension(160, 40));
                
                // Force repaint
                button.revalidate();
                button.repaint();
            } else {
                System.out.println("Button is null or not visible: " + (button != null ? button.getText() : "null"));
            }
        }
        
        // Also force repaint of the entire panel
        productsGrid.revalidate();
        productsGrid.repaint();
    }
}


