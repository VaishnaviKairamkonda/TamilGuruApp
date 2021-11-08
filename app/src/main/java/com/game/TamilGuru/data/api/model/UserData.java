package com.game.TamilGuru.data.api.model;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("app_user_id")
    private int id;
    private String name;
    private String ClassName;
    private String token;
    @SerializedName("app_version")
    private int appVersionCode;
    @SerializedName("profile_type")
    private int profileType;
    private LoginData loginData;

    public UserData(int id, String name, String ClassName, String token, int appVersionCode, int profileType) {
        this.id = id;
        this.name = name;
        this.ClassName = ClassName;
        this.token = token;
        this.appVersionCode = appVersionCode;
        this.profileType=profileType;

    }

    public int getProfileType() {
        return profileType;
    }

    public void setProfileType(int profileType) {
        this.profileType = profileType;
    }

    public LoginData getLoginData() {
        return loginData;
    }

    public void setLoginData(LoginData loginData) {
        this.loginData = loginData;
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
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(int appVersionCode) {
        this.appVersionCode = appVersionCode;
    }
}
