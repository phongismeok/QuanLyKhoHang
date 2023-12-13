package com.example.quanlykhohang.model;

public class Product {
    private int idProduct;
    private String name;
    private int quantity;
    private String pricenhap;
    private String pricexuat;
    private byte[] photo;
    private String status;
    private String dateluukho;
    private String username; // khoa phu

    public Product() {
    }


    public Product(String name, String pricenhap, String pricexuat, byte[] photo, String status, String username,int quantity) {
        this.name = name;
        this.pricenhap = pricenhap;
        this.pricexuat = pricexuat;
        this.photo = photo;
        this.status = status;
        this.username = username;
        this.quantity = quantity;
    }

    public Product(int idProduct, String name, int quantity, String pricenhap, String pricexuat, byte[] photo, String status, String dateluukho, String username) {
        this.idProduct = idProduct;
        this.name = name;
        this.quantity = quantity;
        this.pricenhap = pricenhap;
        this.pricexuat = pricexuat;
        this.photo = photo;
        this.status = status;
        this.dateluukho = dateluukho;
        this.username = username;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPricenhap() {
        return pricenhap;
    }

    public void setPricenhap(String pricenhap) {
        this.pricenhap = pricenhap;
    }

    public String getPricexuat() {
        return pricexuat;
    }

    public void setPricexuat(String pricexuat) {
        this.pricexuat = pricexuat;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateluukho() {
        return dateluukho;
    }

    public void setDateluukho(String dateluukho) {
        this.dateluukho = dateluukho;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
