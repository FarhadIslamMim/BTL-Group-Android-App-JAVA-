package com.oshnisoft.erp.btl.dash;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationModel {

    @SerializedName("employee_id")
    @Expose
    private long employeeId;
    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
