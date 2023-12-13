package com.example.quanlykhohang.model;

public class Bill {
    int idBill; // ma hoa don
    String typeBill;//ghi chu truong hop dac biet
    String createdDate; // ngay tao hoa don
    String note;//ghi chu truong hop dac biet
    String tongtien;
    String status;
    String createdByUser; // tao boi ai

    public Bill() {
    }

    public Bill(String typeBill, String createdDate, String note, String tongtien, String status, String createdByUser) {
        this.typeBill = typeBill;
        this.createdDate = createdDate;
        this.note = note;
        this.tongtien = tongtien;
        this.status = status;
        this.createdByUser = createdByUser;
    }

    public Bill(int idBill, String typeBill, String createdDate, String note, String tongtien, String status, String createdByUser) {
        this.idBill = idBill;
        this.typeBill = typeBill;
        this.createdDate = createdDate;
        this.note = note;
        this.tongtien = tongtien;
        this.status = status;
        this.createdByUser = createdByUser;
    }

    public int getIdBill() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
    }

    public String getTypeBill() {
        return typeBill;
    }

    public void setTypeBill(String typeBill) {
        this.typeBill = typeBill;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTongtien() {
        return tongtien;
    }

    public void setTongtien(String tongtien) {
        this.tongtien = tongtien;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
