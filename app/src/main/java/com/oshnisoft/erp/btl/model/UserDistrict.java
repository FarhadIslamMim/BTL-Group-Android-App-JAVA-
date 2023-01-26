package com.oshnisoft.erp.btl.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserDistrict extends RealmObject {
    @PrimaryKey
    private long id;
    private String name;
    private long store_id;
    private String order_process;

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

    public long getStore_id() {
        return store_id;
    }

    public void setStore_id(long store_id) {
        this.store_id = store_id;
    }

    public String getOrder_process() {
        return order_process;
    }

    public void setOrder_process(String order_process) {
        this.order_process = order_process;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UserDistrict){
            UserDistrict c = (UserDistrict) obj;
            if(c.getName().equals(name) ) return true;
        }

        return false;
    }

}
