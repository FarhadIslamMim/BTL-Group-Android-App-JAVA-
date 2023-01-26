package com.oshnisoft.erp.btl.ui.cashCollection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmitPayment {

    @SerializedName("user_id")
    @Expose
    private long userId;
    @SerializedName("bank_id")
    @Expose
    private long bankId;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("amount")
    @Expose
    private double amount;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBankId() {
        return bankId;
    }

    public void setBankId(long bankId) {
        this.bankId = bankId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
