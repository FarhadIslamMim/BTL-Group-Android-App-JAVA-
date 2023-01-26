package com.oshnisoft.erp.btl.model;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class ChangePassword {

    private String old_password;
    private String password;
    private String password_confirmation;



    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
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

}
