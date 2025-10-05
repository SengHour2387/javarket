package org.example.config;

/**
 * Application constants
 */
public class Constants {
    
    // Database
    public static final String DB_URL = "jdbc:sqlite:database/javarketdata.db";
    
    // User roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_CUSTOMER = "CUSTOMER";
    
    // Order statuses
    public static final String ORDER_STATUS_PENDING = "PENDING";
    public static final String ORDER_STATUS_CONFIRMED = "CONFIRMED";
    public static final String ORDER_STATUS_SHIPPED = "SHIPPED";
    public static final String ORDER_STATUS_DELIVERED = "DELIVERED";
    public static final String ORDER_STATUS_CANCELLED = "CANCELLED";
    
    // Payment methods
    public static final String PAYMENT_CASH = "CASH";
    public static final String PAYMENT_CARD = "CARD";
    public static final String PAYMENT_BANK_TRANSFER = "BANK_TRANSFER";
    
    // Product categories
    public static final String CATEGORY_ELECTRONICS = "ELECTRONICS";
    public static final String CATEGORY_CLOTHING = "CLOTHING";
    public static final String CATEGORY_BOOKS = "BOOKS";
    public static final String CATEGORY_HOME = "HOME";
    public static final String CATEGORY_SPORTS = "SPORTS";
    public static final String CATEGORY_BEAUTY = "BEAUTY";
    public static final String CATEGORY_OTHER = "OTHER";
    
    // UI Constants
    public static final String APP_NAME = "Javarket";
    public static final String APP_VERSION = "1.0.0";
    
    // File paths
    public static final String IMAGES_PATH = "images/";
    public static final String REPORTS_PATH = "reports/";
    
    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    
    // Validation
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MAX_PRODUCT_NAME_LENGTH = 100;
    public static final int MAX_DESCRIPTION_LENGTH = 500;
    
    // Currency
    public static final String CURRENCY_SYMBOL = "$";
    public static final String CURRENCY_CODE = "USD";
}
