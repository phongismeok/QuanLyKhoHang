package com.example.quanlykhohang.model;
/*
* Created by phong 7/10/2023
* */
public class BillDetail {
    private int idBillDetail;
    private int idBill;
    private int idProduct;
    private String quantity;
    private String name;
    private String price;

    public BillDetail() {
    }

    public BillDetail(int idBill, String quantity, String name, String price) {
        this.idBill = idBill;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
    }

    public BillDetail(int idBillDetail, int idBill, int idProduct, String quantity, String name, String price) {
        this.idBillDetail = idBillDetail;
        this.idBill = idBill;
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
    }

    public int getIdBillDetail() {
        return idBillDetail;
    }

    public void setIdBillDetail(int idBillDetail) {
        this.idBillDetail = idBillDetail;
    }

    public int getIdBill() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
