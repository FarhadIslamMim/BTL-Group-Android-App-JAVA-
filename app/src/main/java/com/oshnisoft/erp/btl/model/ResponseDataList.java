package com.oshnisoft.erp.btl.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseDataList<T> {
    @SerializedName("status")
    private boolean isSuccess;

    @SerializedName("delear_limit")
    private double dealerLimit;

    @SerializedName("delear_due")
    private double delearDue;

    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<T> dataList;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public double getDealerLimit() {
        return dealerLimit;
    }

    public void setDealerLimit(double dealerLimit) {
        this.dealerLimit = dealerLimit;
    }

    public double getDelearDue() {
        return delearDue;
    }

    public void setDelearDue(double delearDue) {
        this.delearDue = delearDue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
