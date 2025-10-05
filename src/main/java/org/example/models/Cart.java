package org.example.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Cart model class representing a shopping cart
 */
public class Cart {
    private int userId;
    private List<CartItem> items;
    private BigDecimal totalAmount;

    // Constructors
    public Cart() {
        this.items = new ArrayList<>();
        this.totalAmount = BigDecimal.ZERO;
    }

    public Cart(int userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
        this.totalAmount = BigDecimal.ZERO;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    // Cart operations
    public void addItem(Product product, int quantity) {
        CartItem existingItem = findItemByProductId(product.getId());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(product.getId(), product.getName(), 
                                          product.getPrice(), quantity);
            items.add(newItem);
        }
        calculateTotal();
    }

    public void removeItem(int productId) {
        items.removeIf(item -> item.getProductId() == productId);
        calculateTotal();
    }

    public void updateQuantity(int productId, int quantity) {
        CartItem item = findItemByProductId(productId);
        if (item != null) {
            if (quantity <= 0) {
                removeItem(productId);
            } else {
                item.setQuantity(quantity);
                calculateTotal();
            }
        }
    }

    public void clear() {
        items.clear();
        totalAmount = BigDecimal.ZERO;
    }

    public int getItemCount() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    private CartItem findItemByProductId(int productId) {
        return items.stream()
                .filter(item -> item.getProductId() == productId)
                .findFirst()
                .orElse(null);
    }

    private void calculateTotal() {
        totalAmount = items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "userId=" + userId +
                ", itemCount=" + getItemCount() +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
