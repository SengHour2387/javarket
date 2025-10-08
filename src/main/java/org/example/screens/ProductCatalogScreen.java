package org.example.screens;

import org.example.models.Product;
import org.example.services.ProductService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Product Catalog Screen for customers to browse products
 */
public class ProductCatalogScreen extends JFrame {
    private ProductService productService;
    private JPanel productPanel;
    private JComboBox<String> categoryComboBox;
    private JTextField searchField;
    private JLabel statusLabel;
    private JButton searchButton;
    private JButton filterButton;
    private JButton refreshButton;

    public ProductCatalogScreen() {
        this.productService = new ProductService();
        initializeComponents();
        setupLayout();
        loadProducts();
        loadCategories();
    }

    private void initializeComponents() {
        setTitle("Javarket - Product Catalog");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Search and filter components
        searchField = new JTextField(20);
        categoryComboBox = new JComboBox<>();
        categoryComboBox.addItem("All Categories");
        
        searchButton = new JButton("Search");
        filterButton = new JButton("Filter by Category");
        refreshButton = new JButton("Refresh");
        
        // Product display panel
        productPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        statusLabel = new JLabel("Loading products...");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Top panel with search and filter
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(new JLabel("Category:"));
        topPanel.add(categoryComboBox);
        topPanel.add(filterButton);
        topPanel.add(refreshButton);

        // Add action listeners
        searchButton.addActionListener(e -> searchProducts());
        filterButton.addActionListener(e -> filterByCategory());
        refreshButton.addActionListener(e -> loadProducts());

        // Center panel with products
        JScrollPane scrollPane = new JScrollPane(productPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Add components
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void loadProducts() {
        productPanel.removeAll();
        List<Product> products = productService.getAllProducts();
        displayProducts(products);
    }

    private void searchProducts() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadProducts();
            return;
        }

        List<Product> products = productService.searchProducts(searchTerm);
        displayProducts(products);
    }

    private void filterByCategory() {
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        if ("All Categories".equals(selectedCategory)) {
            loadProducts();
            return;
        }

        List<Product> products = productService.getProductsByCategory(selectedCategory);
        displayProducts(products);
    }

    private void displayProducts(List<Product> products) {
        productPanel.removeAll();
        
        if (products.isEmpty()) {
            JLabel noProductsLabel = new JLabel("No products found");
            noProductsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noProductsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
            productPanel.add(noProductsLabel);
            statusLabel.setText("No products found");
        } else {
            for (Product product : products) {
                JPanel productCard = createProductCard(product);
                productPanel.add(productCard);
            }
            statusLabel.setText("Found " + products.size() + " products");
        }
        
        productPanel.revalidate();
        productPanel.repaint();
    }

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(300, 200));
        card.setMaximumSize(new Dimension(300, 200));

        // Product image placeholder
        JLabel imageLabel = new JLabel("📦", SwingConstants.CENTER);
        imageLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 48));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        imageLabel.setPreferredSize(new Dimension(280, 100));

        // Product info panel
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        nameLabel.setToolTipText(product.getDescription());
        
        JLabel priceLabel = new JLabel("$" + product.getPrice());
        priceLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        priceLabel.setForeground(Color.BLUE);
        
        JLabel stockLabel = new JLabel("Stock: " + product.getStock());
        stockLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        if (product.getStock() == 0) {
            stockLabel.setForeground(Color.RED);
        } else if (product.getStock() < 10) {
            stockLabel.setForeground(Color.ORANGE);
        }
        
        JLabel categoryLabel = new JLabel(product.getCategory());
        categoryLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        categoryLabel.setForeground(Color.GRAY);

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(stockLabel);
        infoPanel.add(categoryLabel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addToCartButton = new JButton("Add to Cart");
        JButton viewDetailsButton = new JButton("View Details");
        
        addToCartButton.addActionListener(e -> addToCart(product));
        viewDetailsButton.addActionListener(e -> viewProductDetails(product));
        
        buttonPanel.add(addToCartButton);
        buttonPanel.add(viewDetailsButton);

        // Add components to card
        card.add(imageLabel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private void addToCart(Product product) {
        if (product.getStock() == 0) {
            JOptionPane.showMessageDialog(this, "This product is out of stock!", "Out of Stock", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String quantityStr = JOptionPane.showInputDialog(this, 
            "Enter quantity for " + product.getName() + ":", "Add to Cart", JOptionPane.QUESTION_MESSAGE);
        
        if (quantityStr != null && !quantityStr.trim().isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityStr.trim());
                if (quantity > 0 && quantity <= product.getStock()) {
                    // TODO: Implement cart functionality
                    JOptionPane.showMessageDialog(this, 
                        quantity + " x " + product.getName() + " added to cart!", 
                        "Added to Cart", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid quantity! Available stock: " + product.getStock(), 
                        "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewProductDetails(Product product) {
        String details = String.format(
            "Product Details:\n\n" +
            "Name: %s\n" +
            "Description: %s\n" +
            "Price: $%.2f\n" +
            "Stock: %d\n" +
            "Category: %s\n" +
            "Status: %s",
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.getCategory(),
            product.isActive() ? "Active" : "Inactive"
        );
        
        JOptionPane.showMessageDialog(this, details, "Product Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadCategories() {
        List<String> categories = productService.getAllCategories();
        for (String category : categories) {
            categoryComboBox.addItem(category);
        }
    }
}
