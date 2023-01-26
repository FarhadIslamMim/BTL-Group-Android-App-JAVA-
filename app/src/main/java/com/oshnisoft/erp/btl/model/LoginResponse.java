package com.oshnisoft.erp.btl.model;


import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("status")
    private boolean isSuccess;
    private String message;
    private String token;
    @SerializedName("role")
    private String role;
    @SerializedName("user")
    private User data;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
