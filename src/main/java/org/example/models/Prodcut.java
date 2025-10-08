package org.example.models;

public class Prodcut {

    private int id;
    private String name;
    private String description;
    private double price;
    private String image;
    private int stock;
    private int category_id;
    private int seller_id;

    public Prodcut(int id, String name, String description, double price, String image, int stock, int category_id, int seller_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.stock = stock;
        this.category_id = category_id;
        this.seller_id = seller_id;
    }

    // Constructor for creating new products (without ID)
    public Prodcut(String name, String description, double price, String image, int stock, int category_id, int seller_id) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.stock = stock;
        this.category_id = category_id;
        this.seller_id = seller_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public int getStock() {
        return stock;
    }

    public int getCategory_id() {
        return category_id;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }
}
