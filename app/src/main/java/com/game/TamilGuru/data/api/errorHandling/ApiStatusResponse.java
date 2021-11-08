package com.game.TamilGuru.data.api.errorHandling;

public class ApiStatusResponse {

    private int status;
    private String message;

    public ApiStatusResponse(int status, String message) {
        this.status = status;
        this.message = message;
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
}