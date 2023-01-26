package com.oshnisoft.erp.btl.model;

import java.io.Serializable;

public class AttendanceStatus implements Serializable {

    private String date;
    private int status;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
