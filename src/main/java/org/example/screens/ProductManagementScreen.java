package org.example.screens;

import org.example.models.Product;
import org.example.services.ProductService;
import org.example.utils.Validators;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Product Management Screen for admin users
 */
public class ProductManagementScreen extends JFrame {
    private ProductService productService;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, priceField, stockField;
    private JTextArea descriptionArea;
    private JComboBox<String> categoryComboBox;
    private Product selectedProduct;

    public ProductManagementScreen() {
        this.productService = new ProductService();
        initializeComponents();
        setupLayout();
        loadProducts();
        loadCategories();
    }

    private void initializeComponents() {
        setTitle("Javarket - Product Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Table setup
        String[] columnNames = {"ID", "Name", "Price", "Stock", "Category", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedProduct();
            }
        });

        // Form fields
        nameField = new JTextField(20);
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        priceField = new JTextField(10);
        stockField = new JTextField(10);
        categoryField = new JTextField(15);
        categoryComboBox = new JComboBox<>();

        // Buttons
        JButton addButton = new JButton("Add Product");
        JButton updateButton = new JButton("Update Product");
        JButton deleteButton = new JButton("Delete Product");
        JButton clearButton = new JButton("Clear Form");
        JButton refreshButton = new JButton("Refresh");

        // Add action listeners
        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        clearButton.addActionListener(e -> clearForm());
        refreshButton.addActionListener(e -> loadProducts());

        // Layout
        setupLayout();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Top panel with form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Form fields
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1;
        formPanel.add(stockField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        formPanel.add(categoryComboBox, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(new JButton("Add Product"));
        buttonPanel.add(new JButton("Update Product"));
        buttonPanel.add(new JButton("Delete Product"));
        buttonPanel.add(new JButton("Clear Form"));
        buttonPanel.add(new JButton("Refresh"));

        // Add action listeners to buttons
        for (Component comp : buttonPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                switch (btn.getText()) {
                    case "Add Product" -> btn.addActionListener(e -> addProduct());
                    case "Update Product" -> btn.addActionListener(e -> updateProduct());
                    case "Delete Product" -> btn.addActionListener(e -> deleteProduct());
                    case "Clear Form" -> btn.addActionListener(e -> clearForm());
                    case "Refresh" -> btn.addActionListener(e -> loadProducts());
                }
            }
        }

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        // Add panels to frame
        add(formPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        List<Product> products = productService.getAllProducts();
        for (Product product : products) {
            Object[] row = {
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.isActive() ? "Active" : "Inactive"
            };
            tableModel.addRow(row);
        }
    }

    private void loadCategories() {
        categoryComboBox.removeAllItems();
        List<String> categories = productService.getAllCategories();
        for (String category : categories) {
            categoryComboBox.addItem(category);
        }
    }

    private void loadSelectedProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            int productId = (Integer) tableModel.getValueAt(selectedRow, 0);
            selectedProduct = productService.getProductById(productId);
            if (selectedProduct != null) {
                nameField.setText(selectedProduct.getName());
                descriptionArea.setText(selectedProduct.getDescription());
                priceField.setText(selectedProduct.getPrice().toString());
                stockField.setText(String.valueOf(selectedProduct.getStock()));
                categoryComboBox.setSelectedItem(selectedProduct.getCategory());
            }
        }
    }

    private void addProduct() {
        if (validateForm()) {
            Product product = new Product();
            product.setName(nameField.getText().trim());
            product.setDescription(descriptionArea.getText().trim());
            product.setPrice(new BigDecimal(priceField.getText().trim()));
            product.setStock(Integer.parseInt(stockField.getText().trim()));
            product.setCategory((String) categoryComboBox.getSelectedItem());

            if (productService.createProduct(product)) {
                JOptionPane.showMessageDialog(this, "Product added successfully!");
                clearForm();
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding product!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateProduct() {
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Please select a product to update!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (validateForm()) {
            selectedProduct.setName(nameField.getText().trim());
            selectedProduct.setDescription(descriptionArea.getText().trim());
            selectedProduct.setPrice(new BigDecimal(priceField.getText().trim()));
            selectedProduct.setStock(Integer.parseInt(stockField.getText().trim()));
            selectedProduct.setCategory((String) categoryComboBox.getSelectedItem());

            if (productService.updateProduct(selectedProduct)) {
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
                clearForm();
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating product!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteProduct() {
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this product?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (productService.deleteProduct(selectedProduct.getId())) {
                JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                clearForm();
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting product!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        nameField.setText("");
        descriptionArea.setText("");
        priceField.setText("");
        stockField.setText("");
        categoryComboBox.setSelectedIndex(0);
        selectedProduct = null;
        productTable.clearSelection();
    }

    private boolean validateForm() {
        if (Validators.isEmpty(nameField.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter product name!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (Validators.isEmpty(descriptionArea.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter product description!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validators.isValidPrice(priceField.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validators.isValidStock(stockField.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid stock quantity!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (categoryComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a category!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
