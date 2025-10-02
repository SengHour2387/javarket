package org.example;

import org.example.authenScreens.AuthenScreen;
import org.example.screens.MainFrame;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        AuthenScreen authenScreen = new AuthenScreen();
        
        boolean isLogin = false;
        
        if(isLogin) {
            mainFrame.setVisible(true);
        } else {
            authenScreen.setVisible(true);
        }
        
    }
}