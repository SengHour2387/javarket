package org.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.example.models.Prodcut;
import org.example.models.Shop;

public class ShopManager {
    private DatabaseConnector connector;
    
    public ShopManager() {
        this.connector = new DatabaseConnector();
    }
    

    public boolean userHasShop(int userId) {
        try {
            ResultSet rs = connector.runSelect(
                "SELECT COUNT(*) as count FROM shop_tbl WHERE owner_ID = ?",
                userId
            );
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Get shop by owner ID
    public Shop getShopByOwnerId(int ownerId) {
        try {
            ResultSet rs = connector.runSelect(
                "SELECT * FROM shop_tbl WHERE owner_ID = ?",
                ownerId
            );
            if (rs.next()) {
                return extractShopFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Get all shops
    public List<Shop> getAllShopsByOwnerId(int ownerId) {
        List<Shop> shops = new ArrayList<>();
        try {
            ResultSet rs = connector.runSelect(
                "SELECT * FROM shop_tbl WHERE owner_ID = ? ORDER BY id DESC",
                ownerId
            );
            while (rs.next()) {
                shops.add(extractShopFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shops;
    }
    
    // Get shop count
    public int getShopCount(int ownerId) {
        try {
            ResultSet rs = connector.runSelect(
                "SELECT COUNT(*) as count FROM shop_tbl WHERE owner_ID = ?",
                ownerId
            );
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Get shop by ID
    public Shop getShopById(int shopId) {
        try {
            ResultSet rs = connector.runSelect(
                "SELECT * FROM shop_tbl WHERE id = ?",
                shopId
            );
            if (rs.next()) {
                return extractShopFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //// / /// Create a new shop
    public boolean createShop(Shop shop) {
        try {
            int result = connector.runCUD(
                "INSERT INTO shop_tbl (name, owner_ID, type, address, phone, email, status) VALUES (?, ?, ?, ?, ?, ?, ?)",
                shop.getName(),
                shop.getOwnerId(),
                shop.getType(),
                shop.getAddress(),
                shop.getPhone(),
                shop.getEmail(),
                shop.getStatus() != null ? shop.getStatus() : "active"
            );
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
      ///// Update shop
    public boolean updateShop(Shop shop) {
        try {
            int result = connector.runCUD(
                "UPDATE shop_tbl SET name = ?, type = ?, address = ?, phone = ?, email = ?, status = ? WHERE id = ?",
                shop.getName(),
                shop.getType(),
                shop.getAddress(),
                shop.getPhone(),
                shop.getEmail(),
                shop.getStatus(),
                shop.getId()
            );
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //// Toggle shop status (active/inactive)
    public boolean toggleShopStatus(int shopId, String newStatus) {
        try {
            int result = connector.runCUD(
                "UPDATE shop_tbl SET status = ? WHERE id = ?",
                newStatus,
                shopId
            );
            System.out.println("Shop " + shopId + " status updated to: " + newStatus);
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Quick toggle between active/inactive
    public boolean toggleShopActiveStatus(int shopId, boolean setActive) {
        String newStatus = setActive ? "active" : "inactive";
        return toggleShopStatus(shopId, newStatus);
    }
    
    //// Delete a single shop and all its products
    public boolean deleteShop(int shopId) {
        try {
            ////// First, delete all products associated with this shop
            connector.runCUD("DELETE FROM shop_product_pair WHERE shop_id = ?", shopId);
            
            //// Then delete the shop
            int result = connector.runCUD("DELETE FROM shop_tbl WHERE id = ?", shopId);
            
            System.out.println("Shop deleted: " + shopId);
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    ///// Delete all shops for a user//
    public boolean deleteAllShops(int ownerId) {
        try {
            // Get all shop IDs for this owner
            ResultSet rs = connector.runSelect(
                "SELECT id FROM shop_tbl WHERE owner_ID = ?",
                ownerId
            );
            
            ///// Delete products for each shop
            while (rs.next()) {
                int shopId = rs.getInt("id");
                connector.runCUD("DELETE FROM shop_product_pair WHERE shop_id = ?", shopId);
            }
            
            // Delete all shops for this owner
            int result = connector.runCUD("DELETE FROM shop_tbl WHERE owner_ID = ?", ownerId);
            
            System.out.println("All shops deleted for owner: " + ownerId);
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ////Add product to shop///
    public boolean addProductToShop(int shopId, int productId) {
        try {
            int result = connector.runCUD(
                "INSERT INTO shop_product_pair (shop_id, product_id) VALUES (?, ?)",
                shopId,
                productId
            );
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //// Get all products for a shop///
    public List<Prodcut> getShopProducts(int shopId) {
        List<Prodcut> products = new ArrayList<>();
        try {
            ResultSet rs = connector.runSelect(
                "SELECT p.* FROM products_tbl p " +
                "INNER JOIN shop_product_pair spp ON p.id = spp.product_id " +
                "WHERE spp.shop_id = ?",
                shopId
            );
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    
    ///// Remove product from shop
    public boolean removeProductFromShop(int shopId, int productId) {
        try {
            int result = connector.runCUD(
                "DELETE FROM shop_product_pair WHERE shop_id = ? AND product_id = ?",
                shopId,
                productId
            );
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Shop extractShopFromResultSet(ResultSet rs) throws SQLException {
        Shop shop = new Shop();
        shop.setId(rs.getInt("id"));
        shop.setName(rs.getString("name"));
        shop.setOwnerId(rs.getInt("owner_ID"));
        shop.setType(rs.getString("type"));
        shop.setAddress(rs.getString("address"));
        shop.setPhone(rs.getString("phone"));
        shop.setEmail(rs.getString("email"));
        shop.setStatus(rs.getString("status"));
        return shop;
    }
    
    private Prodcut extractProductFromResultSet(ResultSet rs) throws SQLException {
        return new Prodcut(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getDouble("price"),
            rs.getString("image"),
            rs.getInt("stock"),
            rs.getInt("category_id"),
            rs.getInt("seller_id")
        );
    }
}

