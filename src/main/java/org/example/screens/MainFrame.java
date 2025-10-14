/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package org.example.screens;

import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

/**
 *
 * @author user
 */
public class MainFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainFrame.class.getName());
    private AppController controller;
    private JPanel shopWindow;
    private ShopPanel shopPanel;
    private CartPanel cartPanel;
    private HistoryPanel historyPanel;
    private MyShopPanel myShopPanel;
    private CardLayout cardLayout;
    private boolean isDarkTheme = false;
    /**
     * Creates new form MainFramex
     */
    public MainFrame( AppController controller ) {
        this.controller = controller;
        initComponents();
        init();
    }

    private void init() {
        // Change Drawer to BoxLayout for easier button management
        Drawer.setLayout(new BoxLayout(Drawer, BoxLayout.Y_AXIS));
        
        // Re-add existing buttons in order
        Drawer.removeAll();
        Drawer.add(Box.createVerticalStrut(14));
        Drawer.add(ShopBtn);
        Drawer.add(Box.createVerticalStrut(12));
        Drawer.add(CartBtn);
        Drawer.add(Box.createVerticalStrut(12));
        Drawer.add(HistoryBtn);
        
        // Add "My Shop" button to drawer
        addMyShopButton();
        
        // Add spacer
        Drawer.add(Box.createVerticalStrut(12));
        Drawer.add(jButton4); // Account button
        
        Drawer.revalidate();
        Drawer.repaint();
        
        loadPanels();
        showShopPanel();
    }
    
    private void addMyShopButton() {
        // Create My Shop button with consistent styling
        JButton myShopBtn = new JButton("🏪 My Shop");
        myShopBtn.setFont(myShopBtn.getFont().deriveFont(Font.BOLD, 14f));
        myShopBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        myShopBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, ShopBtn.getPreferredSize().height));
        myShopBtn.addActionListener(e -> showMyShopPanel());
        
        // Apply FlatLaf styling to match other buttons
        myShopBtn.putClientProperty("FlatLaf.style", "arc: 12");
        myShopBtn.setFocusPainted(false);
        myShopBtn.setBorderPainted(false);
        myShopBtn.setContentAreaFilled(true);
        myShopBtn.setOpaque(true);
        
        // Set colors to match drawer theme
        boolean isDark = UIManager.getLookAndFeel().getName().contains("Dark");
        if (isDark) {
            myShopBtn.setBackground(new Color(60, 60, 60));
            myShopBtn.setForeground(Color.WHITE);
        } else {
            myShopBtn.setBackground(new Color(240, 240, 240));
            myShopBtn.setForeground(Color.BLACK);
        }
        
        // Add to drawer with spacing (BoxLayout respects order)
        Drawer.add(Box.createVerticalStrut(12));
        Drawer.add(myShopBtn);
        
        System.out.println("✅ My Shop button added to drawer");
    }

    private void loadPanels() {
        // Create shop panel directly
        shopPanel = new ShopPanel();
        
        // Create cart panel
        cartPanel = new CartPanel( controller );
        
        // Create history panel with controller to filter by current user
        historyPanel = new HistoryPanel(controller);
        
        // Create my shop panel for sellers
        myShopPanel = new MyShopPanel(controller.getCurrentUser());
        
        // Add panels to content with CardLayout
        Content.add(shopPanel, "SHOP");
        Content.add(cartPanel, "CART");
        Content.add(historyPanel, "HISTORY");
        Content.add(myShopPanel, "MYSHOP");
    }
    
    private void showShopPanel() {
        cardLayout = (CardLayout) Content.getLayout();
        cardLayout.show(Content, "SHOP");
        Content.revalidate();
        Content.repaint();
    }
    
    private void showCartPanel() {
        cartPanel.refreshCart();
        cardLayout = (CardLayout) Content.getLayout();
        cardLayout.show(Content, "CART");
        Content.revalidate();
        Content.repaint();
    }
    
    private void showHistoryPanel() {
        historyPanel.refreshHistory();
        cardLayout = (CardLayout) Content.getLayout();
        cardLayout.show(Content, "HISTORY");
        Content.revalidate();
        Content.repaint();
    }
    
    private void showMyShopPanel() {
        cardLayout = (CardLayout) Content.getLayout();
        cardLayout.show(Content, "MYSHOP");
        Content.revalidate();
        Content.repaint();
    }
    
    private void updateMyShopButtonTheme(boolean isDark) {
        // Find and update the My Shop button in the drawer
        if (Drawer != null) {
            Component[] components = Drawer.getComponents();
            for (Component comp : components) {
                if (comp instanceof JButton) {
                    JButton btn = (JButton) comp;
                    if (btn.getText() != null && btn.getText().contains("My Shop")) {
                        if (isDark) {
                            btn.setBackground(new Color(60, 60, 60));
                            btn.setForeground(Color.WHITE);
                        } else {
                            btn.setBackground(new Color(240, 240, 240));
                            btn.setForeground(Color.BLACK);
                        }
                        btn.repaint();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void frameInit() {
        super.frameInit();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Content = new javax.swing.JPanel();
        Drawer = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        HistoryBtn = new javax.swing.JButton();
        CartBtn = new javax.swing.JButton();
        ShopBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        themeBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(51, 255, 51));

        Content.setLayout(new java.awt.CardLayout());
        getContentPane().add(Content, java.awt.BorderLayout.CENTER);
        Content.getAccessibleContext().setAccessibleName("");
        Content.getAccessibleContext().setAccessibleDescription("");

        Drawer.setMinimumSize(new java.awt.Dimension(300, 0));
        Drawer.setPreferredSize(new java.awt.Dimension(100, 600));

        jButton4.setText("Account");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        HistoryBtn.setText("History");
        HistoryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HistoryBtnActionPerformed(evt);
            }
        });

        CartBtn.setText("Cart");
        CartBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CartBtnActionPerformed(evt);
            }
        });

        ShopBtn.setText("Shop");
        ShopBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShopBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DrawerLayout = new javax.swing.GroupLayout(Drawer);
        Drawer.setLayout(DrawerLayout);
        DrawerLayout.setHorizontalGroup(
            DrawerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DrawerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DrawerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton4)
                    .addGroup(DrawerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(HistoryBtn)
                        .addComponent(CartBtn)
                        .addComponent(ShopBtn)))
                .addContainerGap(215, Short.MAX_VALUE))
        );
        DrawerLayout.setVerticalGroup(
            DrawerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DrawerLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(ShopBtn)
                .addGap(12, 12, 12)
                .addComponent(CartBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(HistoryBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 451, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addGap(17, 17, 17))
        );

        getContentPane().add(Drawer, java.awt.BorderLayout.WEST);

        jPanel1.setBackground(new java.awt.Color(113, 163, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(928, 50));

        jLabel3.setFont(new java.awt.Font("Samsung Sharp Sans", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("JavarKet");

        themeBtn.setText("Theme");
        themeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                themeBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(themeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(264, 264, 264)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(419, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(themeBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            com.formdev.flatlaf.FlatLaf laf = (com.formdev.flatlaf.FlatLaf) javax.swing.UIManager.getLookAndFeel();
            AccountFrame accountFrame = new AccountFrame(controller.getCurrentUser(), laf, controller.getConnector());
            accountFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            accountFrame.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to open Account window: " + ex.getMessage(), "Account", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void themeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themeBtnActionPerformed
        try {
            if (isDarkTheme) {
                UIManager.setLookAndFeel(new FlatMacLightLaf());
                themeBtn.setText("🌙 Dark");
                isDarkTheme = false;
            } else {
                UIManager.setLookAndFeel(new FlatMacDarkLaf());
                themeBtn.setText("☀️ Light");
                isDarkTheme = true;
            }

            // Update all components in the frame
            SwingUtilities.updateComponentTreeUI(this);

            // Update main frame background AFTER UI update using invokeLater
            SwingUtilities.invokeLater(() -> {
                updateMainFrameBackground();
            });

            // Update panels manually without resetting their styling
            if (shopPanel != null) {
                shopPanel.updateTheme();
            }
            if (cartPanel != null) {
                cartPanel.updateTheme();
            }
            if (historyPanel != null) {
                SwingUtilities.updateComponentTreeUI(historyPanel);
            }
            if (myShopPanel != null) {
                myShopPanel.updateTheme();
            }

            pack();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to switch theme: " + e.getMessage(), "Theme Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_themeBtnActionPerformed

    private void HistoryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HistoryBtnActionPerformed
        // TODO add your handling code here:
        showHistoryPanel();
    }//GEN-LAST:event_HistoryBtnActionPerformed

    private void CartBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CartBtnActionPerformed
        // TODO add your handling code here:
        showCartPanel();
    }//GEN-LAST:event_CartBtnActionPerformed

    private void ShopBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShopBtnActionPerformed
        // TODO add your handling code here:
        showShopPanel();
    }//GEN-LAST:event_ShopBtnActionPerformed
    
    private void updateMainFrameBackground() {
        // Update the main frame background based on theme
        if (isDarkTheme) {
            getContentPane().setBackground(new Color(45, 45, 45));
            setBackground(new Color(45, 45, 45));
            // Also update the Content panel specifically
            if (Content != null) {
                Content.setBackground(new Color(45, 45, 45));
            }
            // Update the Drawer (sidebar) background
            if (Drawer != null) {
                Drawer.setBackground(new Color(60, 60, 60));
                
                // Update My Shop button colors
                updateMyShopButtonTheme(true);
            }
            // Update the header panel background
            if (jPanel1 != null) {
                jPanel1.setBackground(new Color(60, 60, 60));
            }
            
        } else {
            getContentPane().setBackground(new Color(248, 249, 250));
            setBackground(new Color(248, 249, 250));
            // Also update the Content panel specifically
            if (Content != null) {
                Content.setBackground(new Color(248, 249, 250));
            }
            // Update the Drawer (sidebar) background
            if (Drawer != null) {
                Drawer.setBackground(new Color(255, 255, 255));
                
                // Update My Shop button colors
                updateMyShopButtonTheme(false);
            }
            // Update the header panel background
            if (jPanel1 != null) {
                jPanel1.setBackground(new Color(104, 181, 255));
            }
            
        }
        
        // Force repaint
        getContentPane().revalidate();
        getContentPane().repaint();
        if (Content != null) {
            Content.revalidate();
            Content.repaint();
        }
        if (Drawer != null) {
            Drawer.revalidate();
            Drawer.repaint();
        }
        if (jPanel1 != null) {
            jPanel1.revalidate();
            jPanel1.repaint();
        }
    }

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CartBtn;
    private javax.swing.JPanel Content;
    private javax.swing.JPanel Drawer;
    private javax.swing.JButton HistoryBtn;
    private javax.swing.JButton ShopBtn;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton themeBtn;
    // End of variables declaration//GEN-END:variables
}
