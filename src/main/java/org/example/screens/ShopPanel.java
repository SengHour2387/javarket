package org.example.screens;

import org.example.SimpleProductManager;
import org.example.CartManager;
import org.example.models.Prodcut;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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

        loadProducts();
    }

    private void loadProducts() {
        SimpleProductManager manager = new SimpleProductManager();
        List<Prodcut> products = manager.getAllProducts();

        productsGrid.removeAll();

        for (Prodcut product : products) {
            productsGrid.add(createProductCard(product));
        }

        productsGrid.revalidate();
        productsGrid.repaint();
        manager.close();
    }

    private JComponent createProductCard(Prodcut product) {
        JPanel card = new JPanel();
        Border border =  new JButton().getBorder();
        card.setBorder( border );
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 12f));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel productImg = new JLabel();
        try {
            URL imgUrl = new URL(product.getImage());
            ImageIcon imageIcon = new ImageIcon(imgUrl);
            if(imageIcon.getImage() == null) {
                throw new IOException("Failed to load image from URL: " + imgUrl);
            }
            Image resizedImg = imageIcon.getImage().getScaledInstance(100,80,Image.SCALE_AREA_AVERAGING);
            productImg.setIcon( new ImageIcon(resizedImg) );
        } catch (MalformedURLException e) {
            System.out.println("fail url: " + e.getMessage());
        }
        catch (IOException e) {
            productImg.setText("Empty Image");
            System.out.println("fail img: " + e.getMessage());
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


