package com.oshnisoft.erp.btl.model;

import io.realm.RealmObject;

public class UserData extends RealmObject {

    private String type;
    private String designation;
    private String secondary_mobile_number;
    private double da_amount;
    private long zone_id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSecondary_mobile_number() {
        return secondary_mobile_number;
    }

    public void setSecondary_mobile_number(String secondary_mobile_number) {
        this.secondary_mobile_number = secondary_mobile_number;
    }

    public double getDa_amount() {
        return da_amount;
    }

    public void setDa_amount(double da_amount) {
        this.da_amount = da_amount;
    }

    public long getZone_id() {
        return zone_id;
    }

    public void setZone_id(long zone_id) {
        this.zone_id = zone_id;
    }
}
