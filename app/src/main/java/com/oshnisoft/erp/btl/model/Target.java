package com.oshnisoft.erp.btl.model;

public class Target {

    private double cash_collection;
    private double target;
    private double shortage;
    private double acheive_percent;

    public double getCash_collection() {
        return cash_collection;
    }

    public void setCash_collection(double cash_collection) {
        this.cash_collection = cash_collection;
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double getShortage() {
        return shortage;
    }

    public void setShortage(double shortage) {
        this.shortage = shortage;
    }

    public double getAcheive_percent() {
        return acheive_percent;
    }

    public void setAcheive_percent(double acheive_percent) {
        this.acheive_percent = acheive_percent;
    }
}
