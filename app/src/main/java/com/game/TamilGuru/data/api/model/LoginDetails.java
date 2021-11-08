package com.game.TamilGuru.data.api.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class LoginDetails {

    private int status;

    private String message;

    @SerializedName("data")
    private UserData userData;
    public LoginDetails(int status, String message, UserData userData) {
        this.status = status;
        this.message = message;
        this.userData = userData;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserData getLoginData() {
        return userData;
    }

    public void setLoginData(UserData userData) {
        this.userData = userData;
    }
    public static LoginDetails fromJson(String jsonString) {
        return new Gson().fromJson(jsonString, LoginDetails.class);
    }
}
