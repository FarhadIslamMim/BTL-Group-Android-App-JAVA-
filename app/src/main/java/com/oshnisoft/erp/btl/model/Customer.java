package com.oshnisoft.erp.btl.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Customer extends RealmObject implements Serializable {



//            "photo_url": null,
//            "shop_photo_url": null,
    @PrimaryKey
    private long id;
    private String name;
    private String contact_person;
    private String email;
    private String mobile;
    private String role;
    private String secondary_mobile_number;
    private String secondary_two_mobile_number;
    private long shop_district_id;
    private long shop_upazila_id;
    private String shop_address_line;
    private long permanent_district_id;
    private long present_upazila_id;
    private String present_address_line;
    private long present_district_id;
    private long permanent_upazila_id;
    private String permanent_address_line;
    private String date_of_birth;
    private String photo_url;
    private String shop_photo_url;
    private double credit_limit;
    private long service_point_id;
    private long store_id;
    private long zone_id;

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

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSecondary_mobile_number() {
        return secondary_mobile_number;
    }

    public void setSecondary_mobile_number(String secondary_mobile_number) {
        this.secondary_mobile_number = secondary_mobile_number;
    }

    public String getSecondary_two_mobile_number() {
        return secondary_two_mobile_number;
    }

    public void setSecondary_two_mobile_number(String secondary_two_mobile_number) {
        this.secondary_two_mobile_number = secondary_two_mobile_number;
    }

    public long getShop_district_id() {
        return shop_district_id;
    }

    public void setShop_district_id(long shop_district_id) {
        this.shop_district_id = shop_district_id;
    }

    public long getShop_upazila_id() {
        return shop_upazila_id;
    }

    public void setShop_upazila_id(long shop_upazila_id) {
        this.shop_upazila_id = shop_upazila_id;
    }

    public String getShop_address_line() {
        return shop_address_line;
    }

    public void setShop_address_line(String shop_address_line) {
        this.shop_address_line = shop_address_line;
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

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getShop_photo_url() {
        return shop_photo_url;
    }

    public void setShop_photo_url(String shop_photo_url) {
        this.shop_photo_url = shop_photo_url;
    }

    public double getCredit_limit() {
        return credit_limit;
    }

    public void setCredit_limit(double credit_limit) {
        this.credit_limit = credit_limit;
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

    public long getZone_id() {
        return zone_id;
    }

    public void setZone_id(long zone_id) {
        this.zone_id = zone_id;
    }
}
