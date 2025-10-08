package org.example;

import org.example.authenScreens.AuthenScreen;
<<<<<<< HEAD
import org.example.utils.DatabaseInitializer;
import java.util.logging.Logger;
=======
import org.example.screens.AppController;
import org.example.screens.MainFrame;
>>>>>>> upstream/main

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    public static void main(String[] args) {
<<<<<<< HEAD
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
=======

        DatabaseConnector connector = new DatabaseConnector();
        AppController appController = new AppController(connector);
        AuthenController authenController = new AuthenController(connector);

        MainFrame mainFrame = new MainFrame(appController);
        AuthenScreen authenScreen = new AuthenScreen(authenController);

        boolean isLogin = false;

        if(isLogin) {
            mainFrame.setVisible(true);
        } else {
            authenScreen.setVisible(true);
>>>>>>> upstream/main
        }
    }
}