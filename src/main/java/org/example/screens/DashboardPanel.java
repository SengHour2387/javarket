package org.example.screens;

import org.example.models.Product;
import org.example.services.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Simple dashboard panel with sample widgets to extend later.
 */
public class DashboardPanel extends JPanel {
    private final ProductService productService;
    private final JLabel totalProductsValueLabel;
    private final JLabel lowStockValueLabel;
    private final JTable recentProductsTable;

    public DashboardPanel() {
        this.productService = new ProductService();

        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Header
        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshData());

        JPanel header = new JPanel(new BorderLayout());
        header.add(title, BorderLayout.WEST);
        header.add(refreshButton, BorderLayout.EAST);

        // Metrics cards
        JPanel metricsPanel = new JPanel(new GridLayout(1, 3, 12, 12));
        JPanel totalProductsCard = createMetricCard("Total Products");
        totalProductsValueLabel = createMetricValueLabel(totalProductsCard);

        JPanel lowStockCard = createMetricCard("Low Stock (< 10)");
        lowStockValueLabel = createMetricValueLabel(lowStockCard);

        JPanel quickActionsCard = createMetricCard("Quick Actions");
        JPanel actions = new JPanel(new GridLayout(0, 1, 6, 6));
        JButton goToCatalog = new JButton("Open Catalog");
        goToCatalog.addActionListener(e -> new ProductCatalogScreen().setVisible(true));
        JButton goToManagement = new JButton("Manage Products");
        goToManagement.addActionListener(e -> new ProductManagementScreen().setVisible(true));
        actions.add(goToCatalog);
        actions.add(goToManagement);
        quickActionsCard.add(actions, BorderLayout.CENTER);

        metricsPanel.add(totalProductsCard);
        metricsPanel.add(lowStockCard);
        metricsPanel.add(quickActionsCard);

        // Recent products table
        String[] columns = {"ID", "Name", "Price", "Stock", "Category"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        recentProductsTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(recentProductsTable);
        JPanel recentPanel = new JPanel(new BorderLayout());
        JLabel recentTitle = new JLabel("Recent Products");
        recentTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        recentPanel.add(recentTitle, BorderLayout.NORTH);
        recentPanel.add(tableScroll, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(metricsPanel, BorderLayout.CENTER);
        add(recentPanel, BorderLayout.SOUTH);

        refreshData();
    }

    private JPanel createMetricCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        JLabel label = new JLabel(title);
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        card.add(label, BorderLayout.NORTH);
        return card;
    }

    private JLabel createMetricValueLabel(JPanel parent) {
        JLabel value = new JLabel("-");
        value.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
        value.setHorizontalAlignment(SwingConstants.LEFT);
        parent.add(value, BorderLayout.CENTER);
        return value;
    }

    public void refreshData() {
        List<Product> all = productService.getAllProducts();
        totalProductsValueLabel.setText(String.valueOf(all.size()));

        long lowStock = all.stream().filter(p -> p.getStock() < 10).count();
        lowStockValueLabel.setText(String.valueOf(lowStock));

        DefaultTableModel model = (DefaultTableModel) recentProductsTable.getModel();
        model.setRowCount(0);
        int limit = Math.min(8, all.size());
        for (int i = 0; i < limit; i++) {
            Product p = all.get(i);
            model.addRow(new Object[]{
                p.getId(), p.getName(), p.getPrice(), p.getStock(), p.getCategory()
            });
        }
    }
}


