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
