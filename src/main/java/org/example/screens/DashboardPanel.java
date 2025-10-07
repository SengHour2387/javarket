package org.example.screens;

import org.example.models.Product;
import org.example.services.ProductService;
import org.example.services.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple dashboard panel with sample widgets to extend later.
 */
public class DashboardPanel extends JPanel {
    private final ProductService productService;
    private final UserService userService;
    private final JLabel totalProductsValueLabel;
    private final JLabel lowStockValueLabel;
    private final JLabel totalUsersValueLabel;
    private final JTable recentProductsTable;
    private final JTable lowStockTable;

    public DashboardPanel() {
        this.productService = new ProductService();
        this.userService = new UserService();

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
        JPanel metricsPanel = new JPanel(new GridLayout(1, 4, 12, 12));
        JPanel totalProductsCard = createMetricCard("Total Products");
        totalProductsValueLabel = createMetricValueLabel(totalProductsCard);

        JPanel lowStockCard = createMetricCard("Low Stock (< 10)");
        lowStockValueLabel = createMetricValueLabel(lowStockCard);

        JPanel totalUsersCard = createMetricCard("Total Users");
        totalUsersValueLabel = createMetricValueLabel(totalUsersCard);

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
        metricsPanel.add(totalUsersCard);
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

        // Low stock table
        DefaultTableModel lowStockModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        lowStockTable = new JTable(lowStockModel);
        JScrollPane lowStockScroll = new JScrollPane(lowStockTable);
        JPanel lowStockPanel = new JPanel(new BorderLayout());
        JLabel lowStockTitle = new JLabel("Low Stock Products");
        lowStockTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        lowStockPanel.add(lowStockTitle, BorderLayout.NORTH);
        lowStockPanel.add(lowStockScroll, BorderLayout.CENTER);

        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 12, 12));
        tablesPanel.add(recentPanel);
        tablesPanel.add(lowStockPanel);

        add(header, BorderLayout.NORTH);
        // Place metrics at the top and tables take the main space
        add(metricsPanel, BorderLayout.NORTH);
        add(tablesPanel, BorderLayout.CENTER);

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
        List<Product> all = productService.getAllProducts(); // SQL-backed via DAO
        totalProductsValueLabel.setText(String.valueOf(all.size()));

        long lowStock = all.stream().filter(p -> p.getStock() < 10).count();
        lowStockValueLabel.setText(String.valueOf(lowStock));

        // Total users from SQL
        int totalUsers = userService.getAllUsers().size();
        totalUsersValueLabel.setText(String.valueOf(totalUsers));

        // Out-of-stock metric (exactly 0)
        long outOfStock = all.stream().filter(p -> p.getStock() == 0).count();
        // If you want a label for out-of-stock, ensure the card exists; otherwise keep internal
        // We'll expose it in a tooltip on the lowStock card's label
        lowStockValueLabel.setToolTipText("Out of stock: " + outOfStock);

        // Recent products table (already ordered by created_at DESC in DAO)
        DefaultTableModel recentModel = (DefaultTableModel) recentProductsTable.getModel();
        recentModel.setRowCount(0);
        int recentLimit = Math.min(8, all.size());
        for (int i = 0; i < recentLimit; i++) {
            Product p = all.get(i);
            recentModel.addRow(new Object[]{
                p.getId(), p.getName(), p.getPrice(), p.getStock(), p.getCategory()
            });
        }
        if (recentModel.getRowCount() == 0) {
            recentModel.addRow(new Object[]{"-", "No products found", "-", "-", "-"});
        }

        // Low stock table (stock < 10), show up to 8
        DefaultTableModel lowModel = (DefaultTableModel) lowStockTable.getModel();
        lowModel.setRowCount(0);
        List<Product> lowList = all.stream()
            .filter(p -> p.getStock() < 10)
            .limit(8)
            .collect(Collectors.toList());
        for (Product p : lowList) {
            lowModel.addRow(new Object[]{
                p.getId(), p.getName(), p.getPrice(), p.getStock(), p.getCategory()
            });
        }
        if (lowModel.getRowCount() == 0) {
            lowModel.addRow(new Object[]{"-", "No low-stock products", "-", "-", "-"});
        }
    }
}


