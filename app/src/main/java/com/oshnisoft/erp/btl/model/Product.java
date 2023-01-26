package com.oshnisoft.erp.btl.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Product extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private String category;
    private String code;
    private String unit;
    private String image;
    private double depot_price;
    private double dealer_price;
    private double retail_price;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getDepot_price() {
        return depot_price;
    }

    public void setDepot_price(double depot_price) {
        this.depot_price = depot_price;
    }

    public double getDealer_price() {
        return dealer_price;
    }

    public void setDealer_price(double dealer_price) {
        this.dealer_price = dealer_price;
    }

    public double getRetail_price() {
        return retail_price;
    }

    public void setRetail_price(double retail_price) {
        this.retail_price = retail_price;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Product){
            Product c = (Product ) obj;
            if(c.getName().equals(name) ) return true;
        }

        return false;
    }


}
