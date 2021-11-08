package com.game.TamilGuru.data.api.model;

public class LoginApiResponse {

    private int status;

    private String message;

    private int profileType;

    public LoginApiResponse(int status, String message, int profileType) {
        this.status = status;
        this.message = message;
        this.profileType = profileType;
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

    public int getProfileType() {
        return profileType;
    }

    public void setProfileType(int profileType) {
        this.profileType = profileType;
    }
}
