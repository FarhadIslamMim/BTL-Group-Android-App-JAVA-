package com.oshnisoft.erp.btl.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    //employee_id, employee_no
    @PrimaryKey
    private long id;
    private long employee_id;
    private String employee_no;
    private String name;
    private String role;
    private String email;
    private String mobile;
    private String is_employee;
    private String is_hr;
    @SerializedName("userInfo")
    private UserData data;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(long employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_no() {
        return employee_no;
    }

    public void setEmployee_no(String employee_no) {
        this.employee_no = employee_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getIs_employee() {
        return is_employee;
    }

    public void setIs_employee(String is_employee) {
        this.is_employee = is_employee;
    }

    public String getIs_hr() {
        return is_hr;
    }

    public void setIs_hr(String is_hr) {
        this.is_hr = is_hr;
    }

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }
}

