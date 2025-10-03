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
                connection  = DriverManager.getConnection(url);
            } catch (SQLException e) {
                throw new SQLException("Fail connection: " + e.getMessage());
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
        try(
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
            )
        {
                setParameters(preparedStatement,parameters);
                return preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new SQLException( "Error runSelect:" + e.getMessage() );
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
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException( "Error runCUD" + e.getMessage() );
        }
    }
    
}
