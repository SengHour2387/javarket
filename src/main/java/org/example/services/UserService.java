package org.example.services;

import org.example.dao.UserDAO;
import org.example.models.User;
import org.example.utils.Validators;

import java.util.List;
import java.util.logging.Logger;

/**
 * Service class for User business logic
 */
public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Register a new user
     * @param user User object to register
     * @return true if registration successful
     */
    public boolean registerUser(User user) {
        // Validate user data
        if (!isValidUserData(user)) {
            logger.warning("Invalid user data provided for registration");
            return false;
        }

        // Check if email already exists
        if (userDAO.emailExists(user.getEmail())) {
            logger.warning("Email already exists: " + user.getEmail());
            return false;
        }

        // Create user
        boolean success = userDAO.createUser(user);
        if (success) {
            logger.info("User registered successfully: " + user.getEmail());
        } else {
            logger.severe("Failed to register user: " + user.getEmail());
        }
        return success;
    }

    /**
     * Authenticate user login
     * @param email User email
     * @param password User password
     * @return User object if authentication successful, null otherwise
     */
    public User loginUser(String email, String password) {
        // Validate input
        if (Validators.isEmpty(email) || Validators.isEmpty(password)) {
            logger.warning("Empty email or password provided for login");
            return null;
        }

        if (!Validators.isValidEmail(email)) {
            logger.warning("Invalid email format provided for login: " + email);
            return null;
        }

        // Authenticate user
        User user = userDAO.authenticateUser(email, password);
        if (user != null) {
            logger.info("User logged in successfully: " + email);
        } else {
            logger.warning("Login failed for email: " + email);
        }
        return user;
    }

    /**
     * Get user by ID
     * @param id User ID
     * @return User object or null
     */
    public User getUserById(int id) {
        if (id <= 0) {
            logger.warning("Invalid user ID provided: " + id);
            return null;
        }
        return userDAO.getUserById(id);
    }

    /**
     * Get user by email
     * @param email User email
     * @return User object or null
     */
    public User getUserByEmail(String email) {
        if (Validators.isEmpty(email) || !Validators.isValidEmail(email)) {
            logger.warning("Invalid email provided: " + email);
            return null;
        }
        return userDAO.getUserByEmail(email);
    }

    /**
     * Update user profile
     * @param user User object to update
     * @return true if update successful
     */
    public boolean updateUser(User user) {
        if (user == null || user.getId() <= 0) {
            logger.warning("Invalid user data provided for update");
            return false;
        }

        if (!isValidUserData(user)) {
            logger.warning("Invalid user data provided for update");
            return false;
        }

        // Check if email is being changed and if new email already exists
        User existingUser = userDAO.getUserById(user.getId());
        if (existingUser != null && !existingUser.getEmail().equals(user.getEmail())) {
            if (userDAO.emailExists(user.getEmail())) {
                logger.warning("Email already exists: " + user.getEmail());
                return false;
            }
        }

        boolean success = userDAO.updateUser(user);
        if (success) {
            logger.info("User updated successfully: " + user.getEmail());
        } else {
            logger.severe("Failed to update user: " + user.getEmail());
        }
        return success;
    }

    /**
     * Delete user
     * @param id User ID
     * @return true if deletion successful
     */
    public boolean deleteUser(int id) {
        if (id <= 0) {
            logger.warning("Invalid user ID provided for deletion: " + id);
            return false;
        }

        boolean success = userDAO.deleteUser(id);
        if (success) {
            logger.info("User deleted successfully: " + id);
        } else {
            logger.severe("Failed to delete user: " + id);
        }
        return success;
    }

    /**
     * Get all users
     * @return List of User objects
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    /**
     * Check if email exists
     * @param email Email to check
     * @return true if email exists
     */
    public boolean emailExists(String email) {
        if (Validators.isEmpty(email) || !Validators.isValidEmail(email)) {
            return false;
        }
        return userDAO.emailExists(email);
    }

    /**
     * Validate user data
     * @param user User object to validate
     * @return true if valid
     */
    private boolean isValidUserData(User user) {
        if (user == null) {
            return false;
        }

        // Validate email
        if (Validators.isEmpty(user.getEmail()) || !Validators.isValidEmail(user.getEmail())) {
            return false;
        }

        // Validate password
        if (Validators.isEmpty(user.getPassword()) || !Validators.isValidPassword(user.getPassword())) {
            return false;
        }

        // Validate names
        if (Validators.isEmpty(user.getFirstName()) || !Validators.isValidName(user.getFirstName())) {
            return false;
        }

        if (Validators.isEmpty(user.getLastName()) || !Validators.isValidName(user.getLastName())) {
            return false;
        }

        // Validate phone if provided
        if (!Validators.isEmpty(user.getPhone()) && !Validators.isValidPhone(user.getPhone())) {
            return false;
        }

        return true;
    }
}
