package org.example;

import org.example.authenScreens.AuthenScreen;
import org.example.screens.AppController;
import org.example.screens.MainFrame;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        DatabaseConnector connector = new DatabaseConnector();

        AppController appController = new AppController(connector);
        AuthenController authenController = new AuthenController(connector);

        MainFrame mainFrame = new MainFrame(appController);
        AuthenScreen authenScreen = new AuthenScreen(authenController);
        
        boolean isLogin = authenController.signIn();
        
        if(isLogin) {
            mainFrame.setVisible(true);
        } else {
            authenScreen.setVisible(true);
        }
        
    }
}