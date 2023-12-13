package com.example.quanlykhohang.model;

public class User {
    private String username; //khoa chinh
    private String password;
    private String numberphone;
    private String position; // chuc vu 1|0
    private byte [] avatar;
    private String profile; // gioi thieu tom tat

    private String lastLogin; // lan cuoi login
    private String createdDate; // ngay tao tk
    private String lastAction; //hanh dong cuoi tren he thong

    public User() {
    }


    public User(String username, String password, String position, String createdDate) {
        this.username = username;
        this.password = password;
        this.position = position;
        this.createdDate = createdDate;
    }

    public User(String username, String password, String numberphone, String position, byte[] avatar, String profile, String lastLogin, String createdDate, String lastAction) {
        this.username = username;
        this.password = password;
        this.numberphone = numberphone;
        this.position = position;
        this.avatar = avatar;
        this.profile = profile;
        this.lastLogin = lastLogin;
        this.createdDate = createdDate;
        this.lastAction = lastAction;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumberphone() {
        return numberphone;
    }

    public void setNumberphone(String numberphone) {
        this.numberphone = numberphone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }
}
