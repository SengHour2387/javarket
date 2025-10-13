/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example;

import org.example.models.User;
import org.example.tools.PasswordHasher;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 *
 * @author user
 */
public class AuthenController {
    private final DatabaseConnector connector;

    User currentUser = new User();

    public AuthenController( DatabaseConnector connector ) {
        try {
            connector.connect();
            this.connector = connector;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DatabaseConnector getConnector() {
        return connector;
    }

    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean signIn(String email, String password) {
        try {
            if (email == null || email.isBlank() || password == null || password.isBlank()) {
                return false;
            }
            // Query the database for user with the given email
            var resultSet = connector.runSelect(
                "SELECT id, user_name, email, hash_pass, pfp FROM users_tbl WHERE email = ?",
                email
            );
            
            if (resultSet.next()) {
                // Get the stored hashed password
                String storedHash = resultSet.getString("hash_pass");
                String userName = resultSet.getString("user_name");
                
                // Check if the provided password matches the stored hash
                boolean passwordMatches = PasswordHasher.checkPassword(password, storedHash);
                
                if (passwordMatches) {
                    System.out.println("Sign in successful for user: " + userName);
                    currentUser = new User(
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5)
                    );
                    return true;
                } else {
                    System.out.println("Invalid password for email: " + email);
                    return false;
                }
            } else {
                System.out.println("No user found with email: " + email);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error during sign in: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean signUp(User newUser,JPanel parent) {
        try {
            // Hash the password before storing
            String hashedPassword = newUser.getHash_pass();
            
            int run = connector.runCUD("INSERT INTO users_tbl (user_name, email, hash_pass, pfp)\n" +
                            "VALUES (?, ?, ?, ?);",
                    newUser.getUser_name(),
                    newUser.getEmail(),
                    hashedPassword, // Store hashed password instead of plain text
                    newUser.getPfp()
            );
            return run>0;
        } catch (SQLException e) {
            System.out.println("singUp error at int AuthenController" + e.getMessage());
            if(e.getMessage().contains("UNIQUE constraint failed: users_tbl.email")) {
                System.out.println("This Email already exists!");
                if (parent != null) {
                    JDialog emailWarningDialog = new JDialog();
                    JLabel smsLabel  = new JLabel();
                    smsLabel.setText("This Email is already exist!");
                    emailWarningDialog.setContentPane(smsLabel);

                    Point dialogPt = new Point(
                            parent.getLocationOnScreen().x + parent.getSize().width/2 - 100,
                            parent.getLocationOnScreen().y + parent.getSize().height/2 - 40);

                    emailWarningDialog.setLocation(dialogPt);
                    emailWarningDialog.setSize(200,80);
                    emailWarningDialog.setVisible(true);
                    emailWarningDialog.setTitle("Sign Up Fail");
                }
            }
            return false;
        }
    }

    public boolean resetPassword() {
        return false;
    }
}
