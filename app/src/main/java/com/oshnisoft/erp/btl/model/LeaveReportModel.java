package com.oshnisoft.erp.btl.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeaveReportModel {

    @SerializedName("status")
    private boolean isSuccess;
    private String message;
    private List<ILeaveSummary> data;
    @SerializedName("data-list")
    private List<ILeaveDetail> leaveList;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ILeaveSummary> getData() {
        return data;
    }

    public void setData(List<ILeaveSummary> data) {
        this.data = data;
    }

    public List<ILeaveDetail> getLeaveList() {
        return leaveList;
    }

    public void setLeaveList(List<ILeaveDetail> leaveList) {
        this.leaveList = leaveList;
    }
}
