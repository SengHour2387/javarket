/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example;

import java.sql.SQLException;

/**
 *
 * @author user
 */
public class AuthenController {

    public AuthenController( DatabaseConnector connector ) {
        try {
            connector.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public boolean signUp() {
        return false;
    }

    public boolean signIn() {
        return false;
    }

    public boolean resetPassword() {
        return false;
    }
}
