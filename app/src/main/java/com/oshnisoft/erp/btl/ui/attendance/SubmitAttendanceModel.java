package com.oshnisoft.erp.btl.ui.attendance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmitAttendanceModel {

    @SerializedName("employee_id")
    @Expose
    private long employeeId;
    @SerializedName("attendance_date")
    @Expose
    private String attendanceDate;
    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("login_time")
    @Expose
    private String loginTime;
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

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
