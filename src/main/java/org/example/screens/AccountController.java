/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.screens;

import org.example.DatabaseConnector;
import org.example.models.User;
import org.example.models.UserShop;

import java.sql.SQLException;

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
