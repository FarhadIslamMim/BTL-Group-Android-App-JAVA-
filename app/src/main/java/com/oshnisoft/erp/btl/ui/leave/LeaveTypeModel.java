package com.oshnisoft.erp.btl.ui.leave;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class LeaveTypeModel extends RealmObject {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("day_count")
    @Expose
    private int dayCount;
    @SerializedName("remarks")
    @Expose
    private String remarks;

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

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LeaveTypeModel){
            LeaveTypeModel c = (LeaveTypeModel ) obj;
            if(c.getName().equals(name) ) return true;
        }

        return false;
    }
}
