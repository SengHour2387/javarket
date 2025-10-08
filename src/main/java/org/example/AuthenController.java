/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example;

import org.example.models.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 *
 * @author user
 */
public class AuthenController {
    private final DatabaseConnector connector;

    public AuthenController( DatabaseConnector connector ) {
        try {
            connector.connect();
            this.connector = connector;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public boolean signIn()  {
        return false;
    }

    public boolean signUp(User newUser,JPanel parent) {
        try {
            connector.runCUD("INSERT INTO users_tbl (user_name, email, hash_pass, pfp)\n" +
                            "VALUES (?, ?, ?, ?);",
                    newUser.getUser_name(),
                    newUser.getEmail(),
                    newUser.getHash_pass(),
                    newUser.getPfp()
            );
            return true;
        } catch (SQLException e) {
            System.out.println("singUp error at int AuthenController" + e.getMessage());
            if(e.getMessage().contains("UNIQUE constraint failed: users_tbl.email")) {
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
            return false;
        }
    }

    public boolean resetPassword() {
        return false;
    }
}
