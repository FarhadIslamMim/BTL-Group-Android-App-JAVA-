package com.oshnisoft.erp.btl.model;


import com.google.gson.annotations.SerializedName;

public class ResponseData<T> {
    @SerializedName("status")
    private boolean isSuccess;

    private String message;
    @SerializedName("data")
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
