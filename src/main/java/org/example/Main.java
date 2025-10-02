package org.example;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import org.example.authenScreens.AuthenScreen;
import org.example.screens.MainFrame;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            FlatLaf.setup(new FlatMacLightLaf());
            javax.swing.UIManager.put("Component.focusWidth", 1);
            javax.swing.UIManager.put("Button.arc", 12);
            javax.swing.UIManager.put("TextComponent.arc", 12);
            javax.swing.UIManager.put("Panel.background", java.awt.Color.white);
            javax.swing.UIManager.put("Component.arc", 12);
            javax.swing.UIManager.put("Component.accentColor", "#2F80ED");
            com.formdev.flatlaf.FlatLaf.setUseNativeWindowDecorations(true);
        } catch (Exception ignore) {
        }

        MainFrame mainFrame = new MainFrame();
        AuthenScreen authenScreen = new AuthenScreen();

        boolean isLogin = false;

        if (isLogin) {
            mainFrame.setVisible(true);
        } else {
            authenScreen.setVisible(true);
        }

    }
}