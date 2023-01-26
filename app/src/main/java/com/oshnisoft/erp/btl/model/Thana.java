package com.oshnisoft.erp.btl.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Thana extends RealmObject {
    @PrimaryKey
    private long id;
    private long district_id;
    private String district;
    private String name;

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

    public long getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(long district_id) {
        this.district_id = district_id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Thana){
            Thana c = (Thana ) obj;
            return c.getName().equals(name);
        }

        return false;
    }
}
