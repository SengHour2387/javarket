/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example;

import java.sql.*;

/**
 *
 * @author user
 */
public class DatabaseConnector {
    String url = "dbc:sqlite:database/javarketdata.db";
    
    Connection connection;

    public void connect() throws SQLException {
        try {
            connection  = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
}
