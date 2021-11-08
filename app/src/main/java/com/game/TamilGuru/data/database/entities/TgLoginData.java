package com.game.TamilGuru.data.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TG_Login")

public class TgLoginData {
    @PrimaryKey
    @ColumnInfo(name = "login_id")
    private int id;

    @ColumnInfo(name = "login_name")
    private String name;

    @ColumnInfo(name = "class_Name")
    private String className;

    @ColumnInfo(name = "email_id")
    private String email;

    public TgLoginData(int id, String name, String className) {
        this.id=id;
        this.name=name;
    }

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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
