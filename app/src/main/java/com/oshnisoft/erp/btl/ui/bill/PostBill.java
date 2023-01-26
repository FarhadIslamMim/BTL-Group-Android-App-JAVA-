package com.oshnisoft.erp.btl.ui.bill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PostBill implements Serializable {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("da_amount")
    @Expose
    private Integer daAmount;
    @SerializedName("ta_amount")
    @Expose
    private Integer taAmount;
    @SerializedName("daily_summary")
    @Expose
    private String dailySummary;
    @SerializedName("is_holiday")
    @Expose
    private String isHoliday;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getDaAmount() {
        return daAmount;
    }

    public void setDaAmount(Integer daAmount) {
        this.daAmount = daAmount;
    }

    public Integer getTaAmount() {
        return taAmount;
    }

    public void setTaAmount(Integer taAmount) {
        this.taAmount = taAmount;
    }

    public String getDailySummary() {
        return dailySummary;
    }

    public void setDailySummary(String dailySummary) {
        this.dailySummary = dailySummary;
    }

    public String getIsHoliday() {
        return isHoliday;
    }

    public void setIsHoliday(String isHoliday) {
        this.isHoliday = isHoliday;
    }
}
