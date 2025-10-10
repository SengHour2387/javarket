package org.example.screens;

import org.example.SimpleProductManager;
import org.example.CartManager;
import org.example.models.Prodcut;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

// Custom responsive grid layout that adapts to container width
class ResponsiveGridLayout implements LayoutManager2 {
    private int hgap, vgap;
    private int minColumns = 1;
    private int maxColumns = 6;
    private int cardWidth = 180;
    private int cardHeight = 210;

    public ResponsiveGridLayout(int hgap, int vgap) {
        this.hgap = hgap;
        this.vgap = vgap;
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
        
        int cardWidth = (availableWidth - (columns - 1) * hgap) / columns;
        int cardHeight = this.cardHeight;
        
        for (int i = 0; i < parent.getComponentCount(); i++) {
            Component comp = parent.getComponent(i);
            int row = i / columns;
            int col = i % columns;
            
            int x = insets.left + col * (cardWidth + hgap);
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
        
        int width = columns * cardWidth + (columns - 1) * hgap + insets.left + insets.right;
        int height = rows * cardHeight + (rows - 1) * vgap + insets.top + insets.bottom;
        
        return new Dimension(width, height);
    }
}

public class ShopPanel extends JPanel {

    private final JPanel productsGrid;
    private final JScrollPane scrollPane;
    private CartManager cartManager;
    private final JLabel loadingLabel = new JLabel("Loading products…", SwingConstants.CENTER);

    public ShopPanel() {
        setLayout(new BorderLayout());
        cartManager = CartManager.getInstance();

        // Use responsive grid layout that adapts to container width
        productsGrid = new JPanel(new ResponsiveGridLayout(8, 8));
        productsGrid.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

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
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        // Image (optional)
        JLabel imageLabel = createImageLabel(product.getImage());
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 12f));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel productImg = new JLabel();
        productImg.setPreferredSize(new Dimension(100, 80));
        productImg.setOpaque(true);
        productImg.setBackground(new Color(245, 245, 245));
        productImg.setBorder(BorderFactory.createLineBorder(new Color(235, 235, 235)));
        productImg.setHorizontalAlignment(SwingConstants.CENTER);
        productImg.setText("No Image");
        productImg.setForeground(Color.GRAY);
        productImg.setFont(productImg.getFont().deriveFont(10f));
        
        // Try to load image asynchronously
        if (product.getImage() != null && !product.getImage().trim().isEmpty()) {
            SwingWorker<ImageIcon, Void> imgWorker = new SwingWorker<>() {
                @Override
                protected ImageIcon doInBackground() {
                    try {
                        URL imgUrl = new URL(product.getImage());
                        ImageIcon imageIcon = new ImageIcon(imgUrl);
                        if (imageIcon.getIconWidth() > 0 && imageIcon.getIconHeight() > 0) {
                            Image resizedImg = imageIcon.getImage().getScaledInstance(100, 80, Image.SCALE_SMOOTH);
                            return new ImageIcon(resizedImg);
                        } else {
                            System.err.println("Invalid image dimensions for: " + product.getImage());
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to load image from: " + product.getImage() + " - " + e.getMessage());
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
                        }
                    } catch (Exception e) {
                        // Keep placeholder
                    }
                }
            };
            imgWorker.execute();
        }
        productImg.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel(String.format("$%.2f", product.getPrice()));
        priceLabel.setForeground(new Color(20, 120, 60));
        priceLabel.setFont(priceLabel.getFont().deriveFont(Font.BOLD, 11f));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        descArea.setFont(descArea.getFont().deriveFont(10f));
        descArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addToCart = new JButton("Add to cart");
        addToCart.setFont(addToCart.getFont().deriveFont(10f));
        addToCart.setAlignmentX(Component.CENTER_ALIGNMENT);
        addToCart.setBackground(new Color(20, 120, 60));
        addToCart.setForeground(Color.WHITE);
        
        // Add action listener to the button
        addToCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart(product);
            }
        });

        card.add(nameLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(productImg);
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(descArea);
        card.add(Box.createVerticalGlue());
        card.add(Box.createVerticalStrut(6));
        card.add(addToCart);

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
                        icon = new ImageIcon(url);
                    } catch (Exception ignore) {
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
}


