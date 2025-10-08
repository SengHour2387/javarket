package org.example;

import org.example.authenScreens.AuthenScreen;
import org.example.tools.AuthRunner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        DatabaseConnector connector = new DatabaseConnector();
        AuthenController authenController = new AuthenController(connector);

        AuthenScreen authenScreen = new AuthenScreen(authenController, new AuthRunner() {
            @Override
            public void run() {
            }
        });
        authenScreen.setVisible(true);
    }
}