package org.example.screens;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

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
    public MainFrame( AppController controller ) {
        this.controller = controller;
        initComponents();
        init();
    }

    private void init() {
        Drawer.setLayout(new BoxLayout(Drawer, BoxLayout.Y_AXIS));
        Drawer.removeAll();
        Drawer.add(Box.createVerticalStrut(14));
        Drawer.add(ShopBtn);
        Drawer.add(Box.createVerticalStrut(12));
        Drawer.add(CartBtn);
        Drawer.add(Box.createVerticalStrut(12));
        Drawer.add(HistoryBtn);
        Drawer.add(Box.createVerticalStrut(12));
        Drawer.add(MineBtn);
        Drawer.add(Box.createVerticalStrut(12));
        Drawer.add(jButton4);
        Drawer.revalidate();
        Drawer.repaint();
        loadPanels();
        showShopPanel();
    }

    private void loadPanels() {
        shopPanel = new ShopPanel();
        cartPanel = new CartPanel( controller );
        historyPanel = new HistoryPanel(controller);
        myShopPanel = new MyShopPanel(controller.getCurrentUser());
        Content.add(shopPanel, "SHOP");
        Content.add(cartPanel, "CART");
        Content.add(historyPanel, "HISTORY");
        Content.add(myShopPanel, "MYSHOP");
    }
    
    private void showShopPanel() {
        cardLayout = (CardLayout) Content.getLayout();
        cardLayout.show(Content, "SHOP");
        shopPanel.reloadProducts();
        Content.revalidate();
        Content.repaint();
    }
    
    private void showCartPanel() {
        cardLayout = (CardLayout) Content.getLayout();
        cardLayout.show(Content, "CART");
        cartPanel.refreshCart();
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

    @SuppressWarnings("unchecked")
    private void initComponents() {

        Content = new javax.swing.JPanel();
        Drawer = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        HistoryBtn = new javax.swing.JButton();
        CartBtn = new javax.swing.JButton();
        ShopBtn = new javax.swing.JButton();
        MineBtn = new javax.swing.JButton();
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
        Drawer.setMixingCutoutShape(MineBtn.getVisibleRect());
        Drawer.setPreferredSize(new java.awt.Dimension(100, 600));

        jButton4.setText("Account");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        Image hisImage =  new ImageIcon(Objects.requireNonNull(getClass().getResource("/org/example/screens/Icons/shopping-basket_5993918.png"))).getImage();
        ImageIcon hisIcon = new ImageIcon(hisImage.getScaledInstance(20,20,Image.SCALE_SMOOTH));

        HistoryBtn.setIcon(hisIcon);
        HistoryBtn.setText("History");
        HistoryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HistoryBtnActionPerformed(evt);
            }
        });

        Image cartImage =  new ImageIcon(Objects.requireNonNull(getClass().getResource("/org/example/screens/Icons/history_17002066.png"))).getImage();
        ImageIcon cartIcon = new ImageIcon(cartImage.getScaledInstance(20,20,Image.SCALE_SMOOTH));

        CartBtn.setText("Cart");
        CartBtn.setIcon(cartIcon);
        CartBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CartBtnActionPerformed(evt);
            }
        });

        Image shopImage =  new ImageIcon(Objects.requireNonNull(getClass().getResource("/org/example/screens/Icons/shop.png"))).getImage();
        ImageIcon shopIcon =  new ImageIcon(shopImage.getScaledInstance(20,20,Image.SCALE_SMOOTH));

        ShopBtn.setIcon(shopIcon);
        ShopBtn.setText("Shop");
        ShopBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShopBtnActionPerformed(evt);
            }
        });

        Image storeImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/org/example/screens/Icons/store_869432.png"))).getImage();
        ImageIcon storeIcon = new ImageIcon(storeImage.getScaledInstance(20,20,Image.SCALE_SMOOTH));
        MineBtn.setText("Mine");
        MineBtn.setIcon(storeIcon);
        MineBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MineBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DrawerLayout = new javax.swing.GroupLayout(Drawer);
        Drawer.setLayout(DrawerLayout);
        DrawerLayout.setHorizontalGroup(
            DrawerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DrawerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DrawerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DrawerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButton4)
                        .addGroup(DrawerLayout.createSequentialGroup()
                            .addGroup(DrawerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(CartBtn)
                                .addComponent(ShopBtn))
                            .addGap(10, 10, 10)))
                    .addGroup(DrawerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(MineBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(HistoryBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DrawerLayout.setVerticalGroup(
            DrawerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DrawerLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(ShopBtn)
                .addGap(12, 12, 12)
                .addComponent(CartBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(MineBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(HistoryBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addGap(17, 17, 17))
        );

        getContentPane().add(Drawer, java.awt.BorderLayout.WEST);

        jPanel1.setBackground(new java.awt.Color(113, 163, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(928, 50));

        jLabel3.setFont(new java.awt.Font("Samsung Sharp Sans", 1, 24));
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
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            com.formdev.flatlaf.FlatLaf laf = (com.formdev.flatlaf.FlatLaf) javax.swing.UIManager.getLookAndFeel();
            AccountFrame accountFrame = new AccountFrame(controller.getCurrentUser(), laf, controller.getConnector());
            accountFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            accountFrame.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to open Account window: " + ex.getMessage(), "Account", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void themeBtnActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (isDarkTheme) {
                UIManager.setLookAndFeel(new FlatMacLightLaf());
                themeBtn.setText("Dark");
                isDarkTheme = false;
            } else {
                UIManager.setLookAndFeel(new FlatMacDarkLaf());
                themeBtn.setText("Light");
                isDarkTheme = true;
            }
            SwingUtilities.updateComponentTreeUI(this);
            SwingUtilities.invokeLater(() -> {
                updateMainFrameBackground();
            });
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
    }

    private void HistoryBtnActionPerformed(java.awt.event.ActionEvent evt) {
        showHistoryPanel();
    }

    private void CartBtnActionPerformed(java.awt.event.ActionEvent evt) {
        showCartPanel();
    }

    private void ShopBtnActionPerformed(java.awt.event.ActionEvent evt) {
        showShopPanel();
    }

    private void MineBtnActionPerformed(java.awt.event.ActionEvent evt) {
        showMyShopPanel();
    }
    
    private void updateMainFrameBackground() {
        if (isDarkTheme) {
            getContentPane().setBackground(new Color(45, 45, 45));
            setBackground(new Color(45, 45, 45));
            if (Content != null) {
                Content.setBackground(new Color(45, 45, 45));
            }
            if (Drawer != null) {
                Drawer.setBackground(new Color(60, 60, 60));
                updateMyShopButtonTheme(true);
            }
            if (jPanel1 != null) {
                jPanel1.setBackground(new Color(60, 60, 60));
            }
        } else {
            getContentPane().setBackground(new Color(248, 249, 250));
            setBackground(new Color(248, 249, 250));
            if (Content != null) {
                Content.setBackground(new Color(248, 249, 250));
            }
            if (Drawer != null) {
                Drawer.setBackground(new Color(255, 255, 255));
                updateMyShopButtonTheme(false);
            }
            if (jPanel1 != null) {
                jPanel1.setBackground(new Color(104, 181, 255));
            }
        }
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

    private javax.swing.JButton CartBtn;
    private javax.swing.JPanel Content;
    private javax.swing.JPanel Drawer;
    private javax.swing.JButton HistoryBtn;
    private javax.swing.JButton MineBtn;
    private javax.swing.JButton ShopBtn;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton themeBtn;
}
