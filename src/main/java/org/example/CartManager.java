package org.example;

import java.util.ArrayList;
import java.util.List;

import org.example.models.Prodcut;

public class CartManager {
    private List<CartItem> cartItems;
    private static CartManager instance;
    
    private CartManager() {
        this.cartItems = new ArrayList<>();
    }
    
    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }
    
    public void addToCart(Prodcut product, int quantity) {
        
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        
        
        cartItems.add(new CartItem(product, quantity));
    }
    
    public void removeFromCart(int productId) {
        cartItems.removeIf(item -> item.getProduct().getId() == productId);
    }
    
    public void updateQuantity(int productId, int newQuantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                if (newQuantity <= 0) {
                    removeFromCart(productId);
                } else {
                    item.setQuantity(newQuantity);
                }
                return;
            }
        }
    }
    
    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }
    
    public double getTotalPrice() {
        return cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
    
    public int getTotalItems() {
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
    
    public void clearCart() {
        cartItems.clear();
    }
    
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
    

    public static class CartItem {
        private Prodcut product;
        private int quantity;
        
        public CartItem(Prodcut product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
        
        public Prodcut getProduct() {
            return product;
        }
        
        public void setProduct(Prodcut product) {
            this.product = product;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
        
        public double getTotalPrice() {
            return product.getPrice() * quantity;
        }
    }
}
