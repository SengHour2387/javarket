package org.example.models;

import java.util.Date;

public class Shop {
    private int id;
    private String name;
    private int ownerId;
    private String type;
    private String address;
    private String phone;
    private String email;
    private String status;
    
    public Shop() {
    }
    
    public Shop(int id, String name, int ownerId, String type, String address, String phone, String email, String status) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.type = type;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.status = status;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ownerId=" + ownerId +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

