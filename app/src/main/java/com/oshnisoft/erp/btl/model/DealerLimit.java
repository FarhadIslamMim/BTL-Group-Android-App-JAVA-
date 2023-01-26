package com.oshnisoft.erp.btl.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DealerLimit implements Serializable {
    @SerializedName("delear_limit")
    private double dealerLimit;

    @SerializedName("delear_due")
    private double delearDue;

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
}
