package com.game.TamilGuru.data.api.model;

public class LoginUserData {

    private String name;
    private String email;
    private String class_name;
    private int app_version;

    public LoginUserData(String givenName, int appVersion) {
        this.name = givenName;

        this.app_version = appVersion;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public int getApp_version() {
        return app_version;
    }

    public void setApp_version(int app_version) {
        this.app_version = app_version;
    }

    public LoginUserData(String userName) {
        this.name = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
