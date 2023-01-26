package com.oshnisoft.erp.btl.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TradeOffer extends RealmObject {

    @PrimaryKey
    private long id;
    private long product_id;
    private String start_date;
    private String end_date;
    private int by_quantity;
    private double by_percentage;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getBy_quantity() {
        return by_quantity;
    }

    public void setBy_quantity(int by_quantity) {
        this.by_quantity = by_quantity;
    }

    public double getBy_percentage() {
        return by_percentage;
    }

    public void setBy_percentage(double by_percentage) {
        this.by_percentage = by_percentage;
    }
}
