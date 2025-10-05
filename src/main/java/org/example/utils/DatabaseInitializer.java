package org.example.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Database initialization utility
 */
public class DatabaseInitializer {
    private static final Logger logger = Logger.getLogger(DatabaseInitializer.class.getName());

    /**
     * Initialize database with required tables
     */
    public static void initializeDatabase() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Create users table
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    email VARCHAR(255) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    first_name VARCHAR(100) NOT NULL,
                    last_name VARCHAR(100) NOT NULL,
                    phone VARCHAR(20),
                    address TEXT,
                    role VARCHAR(20) DEFAULT 'CUSTOMER',
                    is_active BOOLEAN DEFAULT true,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;

            // Create products table
            String createProductsTable = """
                CREATE TABLE IF NOT EXISTS products (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name VARCHAR(255) NOT NULL,
                    description TEXT,
                    price DECIMAL(10,2) NOT NULL,
                    stock INTEGER DEFAULT 0,
                    category VARCHAR(100) NOT NULL,
                    image_url VARCHAR(500),
                    is_active BOOLEAN DEFAULT true,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;

            // Create orders table
            String createOrdersTable = """
                CREATE TABLE IF NOT EXISTS orders (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    order_number VARCHAR(50) UNIQUE NOT NULL,
                    total_amount DECIMAL(10,2) NOT NULL,
                    status VARCHAR(20) DEFAULT 'PENDING',
                    shipping_address TEXT NOT NULL,
                    payment_method VARCHAR(50) NOT NULL,
                    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(id)
                )
            """;

            // Create order_items table
            String createOrderItemsTable = """
                CREATE TABLE IF NOT EXISTS order_items (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    order_id INTEGER NOT NULL,
                    product_id INTEGER NOT NULL,
                    product_name VARCHAR(255) NOT NULL,
                    price DECIMAL(10,2) NOT NULL,
                    quantity INTEGER NOT NULL,
                    subtotal DECIMAL(10,2) NOT NULL,
                    FOREIGN KEY (order_id) REFERENCES orders(id),
                    FOREIGN KEY (product_id) REFERENCES products(id)
                )
            """;

            // Create cart table
            String createCartTable = """
                CREATE TABLE IF NOT EXISTS cart (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    product_id INTEGER NOT NULL,
                    quantity INTEGER NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(id),
                    FOREIGN KEY (product_id) REFERENCES products(id),
                    UNIQUE(user_id, product_id)
                )
            """;

            // Execute table creation
            stmt.execute(createUsersTable);
            stmt.execute(createProductsTable);
            stmt.execute(createOrdersTable);
            stmt.execute(createOrderItemsTable);
            stmt.execute(createCartTable);

            logger.info("Database tables created successfully");

            // Insert sample data
            insertSampleData(conn);

        } catch (SQLException e) {
            logger.severe("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Insert sample data for testing
     */
    private static void insertSampleData(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            
            // Insert sample admin user
            String insertAdminUser = """
                INSERT OR IGNORE INTO users (email, password, first_name, last_name, role) 
                VALUES ('admin@javarket.com', 'admin123', 'Admin', 'User', 'ADMIN')
            """;

            // Insert sample customer user
            String insertCustomerUser = """
                INSERT OR IGNORE INTO users (email, password, first_name, last_name, role) 
                VALUES ('customer@javarket.com', 'customer123', 'John', 'Doe', 'CUSTOMER')
            """;

            // Insert sample products
            String insertProducts = """
                INSERT OR IGNORE INTO products (name, description, price, stock, category) VALUES
                ('Laptop Pro 15"', 'High-performance laptop with 16GB RAM and 512GB SSD', 1299.99, 10, 'ELECTRONICS'),
                ('Wireless Headphones', 'Noise-cancelling wireless headphones with 30-hour battery', 199.99, 25, 'ELECTRONICS'),
                ('Smartphone X', 'Latest smartphone with 128GB storage and triple camera', 899.99, 15, 'ELECTRONICS'),
                ('Cotton T-Shirt', 'Comfortable 100% cotton t-shirt in various colors', 29.99, 50, 'CLOTHING'),
                ('Denim Jeans', 'Classic blue denim jeans with stretch fit', 79.99, 30, 'CLOTHING'),
                ('Running Shoes', 'Lightweight running shoes with breathable mesh', 129.99, 20, 'SPORTS'),
                ('Java Programming Book', 'Complete guide to Java programming language', 49.99, 40, 'BOOKS'),
                ('Coffee Maker', 'Automatic drip coffee maker with programmable timer', 89.99, 12, 'HOME'),
                ('Face Cream', 'Moisturizing face cream for all skin types', 24.99, 35, 'BEAUTY'),
                ('Yoga Mat', 'Non-slip yoga mat with carrying strap', 39.99, 18, 'SPORTS')
            """;

            // Execute inserts
            stmt.execute(insertAdminUser);
            stmt.execute(insertCustomerUser);
            stmt.execute(insertProducts);

            logger.info("Sample data inserted successfully");

        } catch (SQLException e) {
            logger.warning("Error inserting sample data: " + e.getMessage());
        }
    }

    /**
     * Check if database is properly initialized
     * @return true if database is initialized
     */
    public static boolean isDatabaseInitialized() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Check if all required tables exist
            String checkTables = """
                SELECT name FROM sqlite_master 
                WHERE type='table' AND name IN ('users', 'products', 'orders', 'order_items', 'cart')
            """;

            var rs = stmt.executeQuery(checkTables);
            int tableCount = 0;
            while (rs.next()) {
                tableCount++;
            }

            return tableCount == 5; // All 5 tables should exist

        } catch (SQLException e) {
            logger.severe("Error checking database initialization: " + e.getMessage());
            return false;
        }
    }
}
