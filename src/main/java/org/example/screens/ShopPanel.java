package org.example.screens;

import org.example.SimpleProductManager;
import org.example.models.Prodcut;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShopPanel extends JPanel {

    private final JPanel productsGrid;
    private final JScrollPane scrollPane;

    public ShopPanel() {
        setLayout(new BorderLayout());

        productsGrid = new JPanel(new GridLayout(0, 3, 12, 12));
        productsGrid.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        scrollPane = new JScrollPane(productsGrid);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        loadProducts();
    }

    private void loadProducts() {
        SimpleProductManager manager = new SimpleProductManager();
        List<Prodcut> products = manager.getAllProducts();

        productsGrid.removeAll();

        for (Prodcut product : products) {
            productsGrid.add(createProductCard(product));
        }

        productsGrid.revalidate();
        productsGrid.repaint();
        manager.close();
    }

    private JComponent createProductCard(Prodcut product) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14f));

        JLabel priceLabel = new JLabel(String.format("$%.2f", product.getPrice()));
        priceLabel.setForeground(new Color(20, 120, 60));

        JTextArea descArea = new JTextArea(product.getDescription());
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setOpaque(false);
        descArea.setFont(descArea.getFont().deriveFont(12f));

        JButton addToCart = new JButton("Add to cart");

        card.add(nameLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(descArea);
        card.add(Box.createVerticalGlue());
        card.add(Box.createVerticalStrut(8));
        card.add(addToCart);

        return card;
    }
}


