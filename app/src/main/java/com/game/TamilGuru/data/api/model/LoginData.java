package com.game.TamilGuru.data.api.model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("login_id")
    private int id;

    @SerializedName("login_name")
    private String name;

    @SerializedName("class_Name")
    private String class_Name;

    @SerializedName("email_id")
    private String email;

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

    public String getClass_Name() {
        return class_Name;
    }

    public void setClass_Name(String class_Name) {
        this.class_Name = class_Name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
