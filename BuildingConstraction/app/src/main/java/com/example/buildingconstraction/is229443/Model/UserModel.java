package com.example.buildingconstraction.is229443.Model;

public class UserModel {
    String id;
    String password;

    public UserModel() {
    }

    public UserModel(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
