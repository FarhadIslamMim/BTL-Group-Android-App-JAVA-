package com.oshnisoft.erp.btl.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FieldForce extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private String mobile;
    private String email;
    private String role;
    private long service_point_id;
    private long store_id;
    private String type;
    private String designation;
    private long zone_id;
    private long permanent_district_id;
    private long present_upazila_id;
    private String present_address_line;
    private long present_district_id;
    private long permanent_upazila_id;
    private String permanent_address_line;
    private String secondary_mobile_number;
    private String nid_front_url;
    private String nid_back_url;
    private String photo_url;
    private String joining_date;
    private double da_amount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getService_point_id() {
        return service_point_id;
    }

    public void setService_point_id(long service_point_id) {
        this.service_point_id = service_point_id;
    }

    public long getStore_id() {
        return store_id;
    }

    public void setStore_id(long store_id) {
        this.store_id = store_id;
    }

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

    public long getZone_id() {
        return zone_id;
    }

    public void setZone_id(long zone_id) {
        this.zone_id = zone_id;
    }

    public long getPermanent_district_id() {
        return permanent_district_id;
    }

    public void setPermanent_district_id(long permanent_district_id) {
        this.permanent_district_id = permanent_district_id;
    }

    public long getPresent_upazila_id() {
        return present_upazila_id;
    }

    public void setPresent_upazila_id(long present_upazila_id) {
        this.present_upazila_id = present_upazila_id;
    }

    public String getPresent_address_line() {
        return present_address_line;
    }

    public void setPresent_address_line(String present_address_line) {
        this.present_address_line = present_address_line;
    }

    public long getPresent_district_id() {
        return present_district_id;
    }

    public void setPresent_district_id(long present_district_id) {
        this.present_district_id = present_district_id;
    }

    public long getPermanent_upazila_id() {
        return permanent_upazila_id;
    }

    public void setPermanent_upazila_id(long permanent_upazila_id) {
        this.permanent_upazila_id = permanent_upazila_id;
    }

    public String getPermanent_address_line() {
        return permanent_address_line;
    }

    public void setPermanent_address_line(String permanent_address_line) {
        this.permanent_address_line = permanent_address_line;
    }

    public String getSecondary_mobile_number() {
        return secondary_mobile_number;
    }

    public void setSecondary_mobile_number(String secondary_mobile_number) {
        this.secondary_mobile_number = secondary_mobile_number;
    }

    public String getNid_front_url() {
        return nid_front_url;
    }

    public void setNid_front_url(String nid_front_url) {
        this.nid_front_url = nid_front_url;
    }

    public String getNid_back_url() {
        return nid_back_url;
    }

    public void setNid_back_url(String nid_back_url) {
        this.nid_back_url = nid_back_url;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getJoining_date() {
        return joining_date;
    }

    public void setJoining_date(String joining_date) {
        this.joining_date = joining_date;
    }

    public double getDa_amount() {
        return da_amount;
    }

    public void setDa_amount(double da_amount) {
        this.da_amount = da_amount;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FieldForce){
            FieldForce c = (FieldForce) obj;
            if(c.getName().equals(name) ) return true;
        }

        return false;
    }
}
