/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.screens;

import org.example.DatabaseConnector;
import org.example.models.User;
import org.example.models.UserShop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class AccountController {

    private User currentUser;

    private DatabaseConnector connector;
    public AccountController(DatabaseConnector connector, User currentUser) {
        this.currentUser = currentUser;
        this.connector = connector;
    }

    public boolean updateProfile(String newUserName, String newEmail) {
        try {
            int rows = connector.runCUD(
                    "UPDATE users_tbl SET user_name = ?, email = ? WHERE id = ?",
                    newUserName,
                    newEmail,
                    currentUser.getId()
            );
            if (rows > 0) {
                currentUser.setUser_name(newUserName);
                currentUser.setEmail(newEmail);
            }
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error updating profile: " + e.getMessage());
            return false;
        }
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        try {
            // Verify old password
            var rs = connector.runSelect("SELECT hash_pass FROM users_tbl WHERE id = ?", currentUser.getId());
            if (!rs.next()) return false;
            String currentHash = rs.getString(1);
            if (!org.example.tools.PasswordHasher.checkPassword(oldPassword, currentHash)) return false;

            String newHash = org.example.tools.PasswordHasher.hashPassword(newPassword);
            int rows = connector.runCUD("UPDATE users_tbl SET hash_pass = ? WHERE id = ?", newHash, currentUser.getId());
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error changing password: " + e.getMessage());
            return false;
        }
    }

    public List<UserShop> getMyShops() {
        List <UserShop> myShops = new ArrayList<>();

        try {
            ResultSet resultSet = connector.runSelect("SELECT * FROM shop_tbl WHERE owner_ID = ?",currentUser.getId());
            while (resultSet.next()) {
                myShops.add(
                         new UserShop(
                                 resultSet.getInt("id"),
                                 resultSet.getString("name"),
                                 resultSet.getInt("owner_id"),
                                 resultSet.getString("type"),
                                 resultSet.getString("address"),
                                 resultSet.getString("phone"),
                                 resultSet.getString("email"),
                                 resultSet.getString("status")
                         )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return myShops;
    }


    public boolean addShop(UserShop newShop) {
        try {
            int result = connector.runCUD("INSERT INTO shop_tbl (name, owner_ID, type, address, phone, email, status)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    newShop.getName(),
                    newShop.getOwner_id(),
                    newShop.getType(),
                    newShop.getAddress(),
                    newShop.getPhone(),
                    newShop.getEmail(),
                    newShop.getStatus()
                    );
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Error add a new shop: " + e.getMessage());
            return false;
        }
    }

}
