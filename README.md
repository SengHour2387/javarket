# Javarket - E-commerce Java Application

A comprehensive e-commerce application built with Java Swing and SQLite database.

## Project Structure

```
javarket/
├── src/main/java/org/example/
│   ├── Main.java                          # Application entry point
│   ├── config/
│   │   └── Constants.java                 # Application constants
│   ├── models/                            # Data models
│   │   ├── User.java                      # User model
│   │   ├── Product.java                   # Product model
│   │   ├── Order.java                     # Order model
│   │   ├── OrderItem.java                 # Order item model
│   │   ├── Cart.java                      # Shopping cart model
│   │   └── CartItem.java                  # Cart item model
│   ├── dao/                               # Data Access Objects
│   │   ├── UserDAO.java                   # User database operations
│   │   └── ProductDAO.java                # Product database operations
│   ├── services/                          # Business logic layer
│   │   ├── UserService.java               # User business logic
│   │   └── ProductService.java            # Product business logic
│   ├── utils/                             # Utility classes
│   │   ├── DatabaseConnection.java        # Database connection utility
│   │   ├── DatabaseInitializer.java       # Database initialization
│   │   └── Validators.java                # Input validation utilities
│   ├── authenScreens/                     # Authentication screens
│   │   └── AuthenScreen.java              # Login screen
│   └── screens/                           # Main application screens
│       ├── MainFrame.java                 # Main dashboard
│       ├── ProductManagementScreen.java   # Product management (Admin)
│       └── ProductCatalogScreen.java      # Product catalog (Customer)
├── database/
│   └── javarketdata.db                    # SQLite database
├── pom.xml                                # Maven configuration
└── README.md                              # This file
```

## Features

### Core Features

- **User Authentication**: Login system with email/password validation
- **Product Management**: CRUD operations for products (Admin)
- **Product Catalog**: Browse and search products (Customer)
- **Shopping Cart**: Add products to cart (Coming soon)
- **Order Management**: Place and track orders (Coming soon)

### Technical Features

- **MVC Architecture**: Clean separation of concerns
- **Database Integration**: SQLite with proper connection management
- **Input Validation**: Comprehensive validation for all user inputs
- **Modern UI**: FlatLaf theme with responsive design
- **Error Handling**: Proper error handling and logging

## Database Schema

### Users Table

- `id` (INTEGER PRIMARY KEY)
- `email` (VARCHAR UNIQUE)
- `password` (VARCHAR)
- `first_name` (VARCHAR)
- `last_name` (VARCHAR)
- `phone` (VARCHAR)
- `address` (TEXT)
- `role` (VARCHAR) - ADMIN or CUSTOMER
- `is_active` (BOOLEAN)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Products Table

- `id` (INTEGER PRIMARY KEY)
- `name` (VARCHAR)
- `description` (TEXT)
- `price` (DECIMAL)
- `stock` (INTEGER)
- `category` (VARCHAR)
- `image_url` (VARCHAR)
- `is_active` (BOOLEAN)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Orders Table

- `id` (INTEGER PRIMARY KEY)
- `user_id` (INTEGER FOREIGN KEY)
- `order_number` (VARCHAR UNIQUE)
- `total_amount` (DECIMAL)
- `status` (VARCHAR)
- `shipping_address` (TEXT)
- `payment_method` (VARCHAR)
- `order_date` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Order Items Table

- `id` (INTEGER PRIMARY KEY)
- `order_id` (INTEGER FOREIGN KEY)
- `product_id` (INTEGER FOREIGN KEY)
- `product_name` (VARCHAR)
- `price` (DECIMAL)
- `quantity` (INTEGER)
- `subtotal` (DECIMAL)

### Cart Table

- `id` (INTEGER PRIMARY KEY)
- `user_id` (INTEGER FOREIGN KEY)
- `product_id` (INTEGER FOREIGN KEY)
- `quantity` (INTEGER)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

## Getting Started

### Prerequisites

- Java 24 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository
2. Navigate to the project directory
3. Run `mvn clean install` to build the project
4. Run `mvn exec:java -Dexec.mainClass="org.example.Main"` to start the application

### Default Login Credentials

- **Admin**: email: `admin@javarket.com`, password: `admin123`
- **Customer**: email: `customer@javarket.com`, password: `customer123`

## Usage

### For Administrators

1. Login with admin credentials
2. Navigate to Settings > Product Management
3. Add, edit, or delete products
4. Manage product categories and stock

### For Customers

1. Login with customer credentials
2. Navigate to Products to browse the catalog
3. Search and filter products by category
4. View product details and add to cart (coming soon)

## Development

### Adding New Features

1. Create model classes in `models/` package
2. Create DAO classes in `dao/` package for database operations
3. Create service classes in `services/` package for business logic
4. Create UI screens in `screens/` package
5. Update database schema in `DatabaseInitializer.java`

### Code Style

- Follow Java naming conventions
- Use proper error handling and logging
- Add comprehensive input validation
- Document public methods and classes

## Dependencies

- **SQLite JDBC**: Database connectivity
- **FlatLaf**: Modern UI theme
- **Java Swing**: GUI framework

## Future Enhancements

- [ ] Complete shopping cart functionality
- [ ] Order management system
- [ ] User management interface
- [ ] Payment integration
- [ ] Email notifications
- [ ] Product image upload
- [ ] Advanced search and filtering
- [ ] Order history and tracking
- [ ] Inventory management
- [ ] Sales reporting

## License

This project is for educational purposes only.
