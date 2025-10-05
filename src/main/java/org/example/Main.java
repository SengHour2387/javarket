package org.example;

import org.example.authenScreens.AuthenScreen;
import org.example.utils.DatabaseInitializer;
import java.util.logging.Logger;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    public static void main(String[] args) {
        try {
            // Initialize database
            logger.info("Initializing database...");
            DatabaseInitializer.initializeDatabase();
            logger.info("Database initialized successfully");
            
            // Start the application
            new AuthenScreen().setVisible(true);
        } catch (Exception e) {
            logger.severe("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}