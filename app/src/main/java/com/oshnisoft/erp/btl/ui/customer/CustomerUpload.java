package com.oshnisoft.erp.btl.ui.customer;

public class CustomerUpload {

    private String code;
    private String name;
    private String contact_person;
    private String mobile;
    private String secondary_mobile_number;
    private long new_contact_id;
    private long shop_district_id;
    private long shop_upazila_id;
    private String shop_address_line;
    private String photo_url;
    private String shop_photo_url;
    private String password;
    private String password_confirmation;
    private String status;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSecondary_mobile_number() {
        return secondary_mobile_number;
    }

    public void setSecondary_mobile_number(String secondary_mobile_number) {
        this.secondary_mobile_number = secondary_mobile_number;
    }

    public long getNew_contact_id() {
        return new_contact_id;
    }

    public void setNew_contact_id(long new_contact_id) {
        this.new_contact_id = new_contact_id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
