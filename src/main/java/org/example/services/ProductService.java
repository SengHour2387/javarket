package org.example.services;

import org.example.dao.ProductDAO;
import org.example.models.Product;
import org.example.utils.Validators;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service class for Product business logic
 */
public class ProductService {
    private static final Logger logger = Logger.getLogger(ProductService.class.getName());
    private final ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    /**
     * Create a new product
     * @param product Product object to create
     * @return true if creation successful
     */
    public boolean createProduct(Product product) {
        // Validate product data
        if (!isValidProductData(product)) {
            logger.warning("Invalid product data provided for creation");
            return false;
        }

        boolean success = productDAO.createProduct(product);
        if (success) {
            logger.info("Product created successfully: " + product.getName());
        } else {
            logger.severe("Failed to create product: " + product.getName());
        }
        return success;
    }

    /**
     * Get product by ID
     * @param id Product ID
     * @return Product object or null
     */
    public Product getProductById(int id) {
        if (id <= 0) {
            logger.warning("Invalid product ID provided: " + id);
            return null;
        }
        return productDAO.getProductById(id);
    }

    /**
     * Get all products
     * @return List of Product objects
     */
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    /**
     * Get products by category
     * @param category Product category
     * @return List of Product objects
     */
    public List<Product> getProductsByCategory(String category) {
        if (Validators.isEmpty(category)) {
            logger.warning("Empty category provided for product search");
            return List.of();
        }
        return productDAO.getProductsByCategory(category);
    }

    /**
     * Search products
     * @param searchTerm Search term
     * @return List of Product objects
     */
    public List<Product> searchProducts(String searchTerm) {
        if (Validators.isEmpty(searchTerm)) {
            logger.warning("Empty search term provided");
            return List.of();
        }
        return productDAO.searchProducts(searchTerm);
    }

    /**
     * Update product
     * @param product Product object to update
     * @return true if update successful
     */
    public boolean updateProduct(Product product) {
        if (product == null || product.getId() <= 0) {
            logger.warning("Invalid product data provided for update");
            return false;
        }

        if (!isValidProductData(product)) {
            logger.warning("Invalid product data provided for update");
            return false;
        }

        boolean success = productDAO.updateProduct(product);
        if (success) {
            logger.info("Product updated successfully: " + product.getName());
        } else {
            logger.severe("Failed to update product: " + product.getName());
        }
        return success;
    }

    /**
     * Update product stock
     * @param productId Product ID
     * @param newStock New stock quantity
     * @return true if update successful
     */
    public boolean updateStock(int productId, int newStock) {
        if (productId <= 0) {
            logger.warning("Invalid product ID provided for stock update: " + productId);
            return false;
        }

        if (newStock < 0) {
            logger.warning("Invalid stock quantity provided: " + newStock);
            return false;
        }

        boolean success = productDAO.updateStock(productId, newStock);
        if (success) {
            logger.info("Product stock updated successfully: " + productId + " -> " + newStock);
        } else {
            logger.severe("Failed to update product stock: " + productId);
        }
        return success;
    }

    /**
     * Reduce product stock
     * @param productId Product ID
     * @param quantity Quantity to reduce
     * @return true if reduction successful
     */
    public boolean reduceStock(int productId, int quantity) {
        if (productId <= 0 || quantity <= 0) {
            logger.warning("Invalid parameters for stock reduction: productId=" + productId + ", quantity=" + quantity);
            return false;
        }

        Product product = productDAO.getProductById(productId);
        if (product == null) {
            logger.warning("Product not found for stock reduction: " + productId);
            return false;
        }

        if (product.getStock() < quantity) {
            logger.warning("Insufficient stock for reduction: available=" + product.getStock() + ", requested=" + quantity);
            return false;
        }

        int newStock = product.getStock() - quantity;
        return updateStock(productId, newStock);
    }

    /**
     * Add product stock
     * @param productId Product ID
     * @param quantity Quantity to add
     * @return true if addition successful
     */
    public boolean addStock(int productId, int quantity) {
        if (productId <= 0 || quantity <= 0) {
            logger.warning("Invalid parameters for stock addition: productId=" + productId + ", quantity=" + quantity);
            return false;
        }

        Product product = productDAO.getProductById(productId);
        if (product == null) {
            logger.warning("Product not found for stock addition: " + productId);
            return false;
        }

        int newStock = product.getStock() + quantity;
        return updateStock(productId, newStock);
    }

    /**
     * Delete product (soft delete)
     * @param id Product ID
     * @return true if deletion successful
     */
    public boolean deleteProduct(int id) {
        if (id <= 0) {
            logger.warning("Invalid product ID provided for deletion: " + id);
            return false;
        }

        boolean success = productDAO.deleteProduct(id);
        if (success) {
            logger.info("Product deleted successfully: " + id);
        } else {
            logger.severe("Failed to delete product: " + id);
        }
        return success;
    }

    /**
     * Get all categories
     * @return List of unique categories
     */
    public List<String> getAllCategories() {
        return productDAO.getAllCategories();
    }

    /**
     * Check if product is in stock
     * @param productId Product ID
     * @return true if in stock
     */
    public boolean isInStock(int productId) {
        Product product = getProductById(productId);
        return product != null && product.isInStock();
    }

    /**
     * Get available stock for product
     * @param productId Product ID
     * @return Available stock quantity
     */
    public int getAvailableStock(int productId) {
        Product product = getProductById(productId);
        return product != null ? product.getStock() : 0;
    }

    /**
     * Validate product data
     * @param product Product object to validate
     * @return true if valid
     */
    private boolean isValidProductData(Product product) {
        if (product == null) {
            return false;
        }

        // Validate name
        if (Validators.isEmpty(product.getName()) || !Validators.isValidProductName(product.getName())) {
            return false;
        }

        // Validate description
        if (Validators.isEmpty(product.getDescription()) || product.getDescription().length() > 500) {
            return false;
        }

        // Validate price
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // Validate stock
        if (product.getStock() < 0) {
            return false;
        }

        // Validate category
        if (Validators.isEmpty(product.getCategory())) {
            return false;
        }

        return true;
    }
}
