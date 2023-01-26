package com.oshnisoft.erp.btl.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeavePostModel {

    @SerializedName("employee_id")
    @Expose
    private long empID;

    @SerializedName("leave_type_id")
    @Expose
    private long leaveTypeID;

    @SerializedName("purpose")
    @Expose
    private String purpose;

    @SerializedName("application_date")
    @Expose
    private String applyDate;

    @SerializedName("start_date")
    @Expose
    private String fromDate;

    @SerializedName("end_date")
    @Expose
    private String toDate;

    public long getEmpID() {
        return empID;
    }

    public void setEmpID(long empID) {
        this.empID = empID;
    }

    public long getLeaveTypeID() {
        return leaveTypeID;
    }

    public void setLeaveTypeID(long leaveTypeID) {
        this.leaveTypeID = leaveTypeID;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
