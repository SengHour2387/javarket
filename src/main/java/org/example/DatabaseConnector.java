/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example;

import java.sql.*;
import java.util.Objects;

/**
 *
 * @author user
 */
public class DatabaseConnector {
    String url = "jdbc:sqlite:database/javarketdata.db";
    
    Connection connection;

    public void connect() throws SQLException {
        if( connection == null || connection.isClosed() ) {
            try {
                System.out.println("Connecting to database: " + url);
                connection  = DriverManager.getConnection(url);
                System.out.println("Database connection successful");
            } catch (SQLException e) {
                System.err.println("Database connection failed: " + e.getMessage());
                e.printStackTrace();
                throw new SQLException("Failed to connect to database at " + url + ": " + e.getMessage(), e);
            }
        }
    }

    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }

    // for Read ( just read from database )
    public ResultSet runSelect(String sql, Object...parameters) throws SQLException {
        connect();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            setParameters(preparedStatement,parameters);
            System.out.println("SQL query executed: " + sql.substring(0, Math.min(sql.length(), 50)) + "...");
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println("SQL query failed: " + sql);
            System.err.println("Parameters: " + java.util.Arrays.toString(parameters));
            e.printStackTrace();
            throw new SQLException("Error executing query: " + e.getMessage(), e);
        }
    }

    // for Create Update Delete ( database makes changes )
    public int runCUD(String sql, Object...parameters) throws SQLException {
        connect();
        try(
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        )
        {
            setParameters(preparedStatement,parameters);
            int result = preparedStatement.executeUpdate();
            System.out.println("SQL executed successfully: " + sql.substring(0, Math.min(sql.length(), 50)) + "...");
            return result;
        } catch (SQLException e) {
            System.err.println("SQL execution failed: " + sql);
            System.err.println("Parameters: " + java.util.Arrays.toString(parameters));
            e.printStackTrace();
            throw new SQLException("Error executing SQL: " + e.getMessage(), e);
        }
    }
    
}
